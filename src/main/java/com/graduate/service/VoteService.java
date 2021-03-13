package com.graduate.service;

import com.graduate.base.IResponse;
import com.graduate.model.PostVotes;
import com.graduate.model.PostVotesRepository;
import com.graduate.response.ActionResultTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class VoteService {

    private final PostVotesRepository votesRepository;

    public VoteService(@Autowired PostVotesRepository postVotesRepository) {
        votesRepository = postVotesRepository;
    }

    public IResponse vote(int postId, int userId, byte value) {
        Optional<PostVotes> postVote = votesRepository.getVote(userId, postId);
        PostVotes pv = postVote.orElseGet(PostVotes::new);
        if (pv.getValue() == 0) {
            pv.setPostId(postId);
            pv.setUserId(userId);
            pv.setValue(value);
            pv.setTime(LocalDateTime.now());
        } else {
            if (pv.getValue() == value) {
                return new ActionResultTemplate(false);
            }
            pv.setValue(value);
        }
        votesRepository.save(pv);
        return new ActionResultTemplate(true);
    }
}
