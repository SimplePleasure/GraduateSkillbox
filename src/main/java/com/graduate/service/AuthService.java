package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.model.PostRepository;
import com.graduate.model.User;
import com.graduate.model.UserRepository;
import com.graduate.request.ChangePassword;
import com.graduate.response.AuthCheck;
import com.graduate.response.ActionResult;
import com.graduate.response.UserInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private MailService mailService;

    public AuthService(@Autowired UserRepository userRepository, @Autowired PostRepository postRepository,
                       @Autowired MailService mailService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.mailService = mailService;
    }

    public IResponse checkAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return new ActionResult(false);
        }
        User user = userRepository.getUserByEmail(((UserDetails)authentication.getPrincipal()).getUsername());
        UserInfo userInfo = new UserInfo(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, 0, user.getIsModerator() == 1);
        if (user.getIsModerator() == 1) {
            userInfo.setModerationCount(postRepository.getPostsCountWaitingModeration());
        }
        return new AuthCheck(true, userInfo);
    }

    public IResponse restorePassword(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            String hash = DigestUtils.md5Hex(user.toString());
            user.setCode(hash);
            userRepository.save(user);
            mailService.sendMessage(email, hash);
            return new ActionResult(true);
        }
        return new ActionResult(false);
    }

    public IResponse changePassword(ChangePassword changePassword) {
        User user = userRepository.getUserByCode(changePassword.getCode());
        if (user != null) { // TODO: 14.02.2021 implement capcha check
            // TODO: 14.02.2021 add password crypting
            user.setPassword(changePassword.getPassword());
            user.setCode(null);
            userRepository.save(user);
            return new ActionResult(true);
        }
        return new ActionResult(false);
    }

}
