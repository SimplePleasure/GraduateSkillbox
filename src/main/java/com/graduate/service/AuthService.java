package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.exceptionHandler.exceptions.UserNotAuthException;
import com.graduate.model.User;
import com.graduate.model.UserRepository;
import com.graduate.request.ChangePassword;
import com.graduate.response.ActionResultTemplateWithErrors;
import com.graduate.response.AuthCheck;
import com.graduate.response.ActionResultTemplate;
import com.graduate.response.UserInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class AuthService {

    @Value("${project.uploads-directory}")
    private String UPLOADS;
    private String UPLOADS_FOLDER = "upload/";

    private final UserRepository userRepository;
    private final MailService mailService;

    public AuthService(@Autowired UserRepository userRepository, @Autowired MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
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
            throw new UserNotAuthException("executing action required auth"); // TODO: 02.03.2021 implement receiving text from separate file
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
            String hash = DigestUtils.md5Hex(user.toString());
            user.setCode(hash);
            userRepository.save(user);
            mailService.sendMessage(email, hash);
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
        User user = getCurrentUserOrThrow();
        String imgHash = String.valueOf(image.hashCode());
        String generatedPath = imgHash.substring(0, 3) + "/" + imgHash.substring(3, 6) + "/" + imgHash.substring(6, 9) + "/";

        try {
            new File(UPLOADS + generatedPath).mkdirs();
            Files.write(Path.of(UPLOADS + generatedPath + image.getOriginalFilename()), image.getBytes());
        } catch (IOException e) {
            throw new MultipartException("was a problem while file saving");
        }
        user.setPhoto(UPLOADS_FOLDER + generatedPath + image.getOriginalFilename());
        user = userRepository.save(user);
        return user.getPhoto();
    }

}
