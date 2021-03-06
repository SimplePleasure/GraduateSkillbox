package com.graduate.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.graduate.base.IResponse;
import com.graduate.core.CaptchaResizer;
import com.graduate.core.ResponseFormer;
import com.graduate.exceptionHandler.exceptions.CaptchaNotFoundException;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.model.CaptchaCode;
import com.graduate.model.CaptchaCodeRepository;
import com.graduate.model.User;
import com.graduate.model.UserRepository;
import com.graduate.request.ChangePassword;
import com.graduate.request.Register;
import com.graduate.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${project.uploads-directory}")
    private String UPLOADS;
    @Value("${project.captcha-type}")
    private String captchaType;
    @Value("${project.captcha-life-cycle-in-minutes}")
    private String captchaLifeCycleInMinutes;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final CaptchaCodeRepository captchaCodeRepository;

    public AuthService(@Autowired UserRepository userRepository, @Autowired MailService mailService,
                       @Autowired CaptchaCodeRepository captchaCodeRepository) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.captchaCodeRepository = captchaCodeRepository;
    }



    public IResponse checkAuth(int postsCountWaitingModeration) {
        if (!isUserAuth()) {
            return new ActionResultTemplate(false);
        }
        User user = userRepository.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        UserInfo ui = new UserInfo(user.getId(), user.getName(), user.getPhoto(), user.getEmail(), user.isModerator(),
                postsCountWaitingModeration, user.isModerator());
        return new AuthCheck(true, ui);
    }

    public User getCurrentUserOrThrow() {
        if (!isUserAuth()) {
            throw new UserNotAuthException("executing action required auth");
        }
        return userRepository.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public int getUserIdOrZero() {
        return isUserAuth() ?
                userRepository.getUserId(SecurityContextHolder.getContext().getAuthentication().getName()) : 0;
    }

    private boolean isUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    public IResponse restorePassword(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            String hash = UUID.randomUUID().toString().replaceAll("-", "");
            user.setCode(hash);
            userRepository.save(user);
            mailService.sendPassRecoveryMessage(email, hash); // FIXME: 06.03.2021 edit send link
            return new ActionResultTemplate(true);
        }
        return new ActionResultTemplate(false);
    }

    public IResponse changePassword(ChangePassword changePassword) {
        User user = userRepository.getUserByCode(changePassword.getCode());
        if (user != null) { // TODO: 14.02.2021 implement capcha check
            // TODO: 14.02.2021 add password crypting
            user.setPassword(changePassword.getPassword());
            user.setCode(null);
            userRepository.save(user);
            return new ActionResultTemplate(true);
        }
        ActionResultTemplateWithErrors err = new ActionResultTemplateWithErrors(false);
        err.addError("code", "password recovery link might be deprecated. " +
                "<a href=\"/auth/restore\"> request new link </a>");
        return err;
    }

    public String uploadImage(MultipartFile image) {
        try {
            if (!image.isEmpty() &&
                    (Objects.requireNonNull(image.getContentType()).equalsIgnoreCase(MediaType.IMAGE_PNG_VALUE) ||
                            image.getContentType().equalsIgnoreCase(MediaType.IMAGE_JPEG_VALUE))) {
                User user = getCurrentUserOrThrow();
                String randomString = UUID.randomUUID().toString().replaceAll("-", "");
                String generatedPath = randomString.substring(0, 5) + "/" +
                        randomString.substring(5, 10) + "/" + randomString.substring(10, 15) + "/";
                new File(UPLOADS + generatedPath).mkdirs();
                Files.write(Path.of(UPLOADS + generatedPath + image.getOriginalFilename()), image.getBytes());
                String UPLOADS_FOLDER = "upload/";
                user.setPhoto(UPLOADS_FOLDER + generatedPath + image.getOriginalFilename());
                userRepository.save(user);
                return user.getPhoto();
            }
            throw new MultipartException("file extension must be jpg/png");
        } catch (IOException e) {
            throw new MultipartException("was a problem while file saving. " + e.getMessage());
        }

    }

    public IResponse register(Register register) { // TODO: 05.03.2021 implement method
        CaptchaCode captcha = captchaCodeRepository.getCaptchaBySecretCode(register.getCaptchaSecret())
                .orElseThrow(CaptchaNotFoundException::new); // TODO: 06.03.2021 not handling exception

        boolean isPasswordLengthCorrect = register.getPassword().length() > 5;
        boolean isEmailAddressIsFree = !userRepository.isEmailAlreadyExists(register.getEmail());
        boolean isCaptchaCodeCorrect = register.getCaptcha().equals(captcha.getCode());
        boolean isCaptchaTimeCorrect = captcha.getTime().isAfter(LocalDateTime.now());

        if (isPasswordLengthCorrect && isEmailAddressIsFree && isCaptchaCodeCorrect && isCaptchaTimeCorrect) {
            User user = new User();
            user.setEmail(register.getEmail());
            user.setName(register.getName());
            user.setPassword(register.getPassword());
            user.setIsModerator((byte) 0);
            user.setRegTime(LocalDateTime.now());
            userRepository.save(user);
            mailService.sayHiMessage(user.getEmail());
            return new ActionResultTemplate(true);
        }
        return ResponseFormer.getErrFromRegister(isPasswordLengthCorrect,
                isEmailAddressIsFree, isCaptchaCodeCorrect, isCaptchaTimeCorrect);
    }

    public IResponse generateCaptcha() {
        Cage cage = new GCage();
        String secretCode = UUID.randomUUID().toString().substring(0, 12);
        String generatedCode = cage.getTokenGenerator().next();

//        byte[] generatedImg = cage.draw(generatedCode);
        byte[] generatedImg = CaptchaResizer.resizeCaptcha(70, 35, cage.drawImage(generatedCode));
        String generatedCaptcha = captchaType + Base64.getEncoder().encodeToString(generatedImg);

        LocalDateTime validTo = LocalDateTime.now().plusMinutes(Integer.parseInt(captchaLifeCycleInMinutes));
        CaptchaCode code = new CaptchaCode();
        code.setSecretCode(secretCode);
        code.setCode(generatedCode);
        code.setTime(validTo);
        captchaCodeRepository.save(code);
        return new CaptchaCheck(secretCode, generatedCaptcha);
    }

}
