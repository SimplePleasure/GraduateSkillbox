package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.model.PostRepository;
import com.graduate.model.User;
import com.graduate.model.UserRepository;
import com.graduate.response.AuthCheck;
import com.graduate.response.Denied;
import com.graduate.response.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public AuthService(@Autowired UserRepository userRepository, @Autowired PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public IResponse checkAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return new Denied();
        }
        User user = userRepository.getUserByEmail(
                ((UserDetails)authentication.getPrincipal()).getUsername());
        int moderationCount = 0;
        if (user.getIsModerator() == 1) {
            moderationCount = postRepository.getPostsCountWaitingModeration();
        }

        // TODO: 11.02.2021 implement receive userInfo's access to settings
        UserInfo userInfo = new UserInfo(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, moderationCount, false);

        return new AuthCheck(true, userInfo);
    }
}
