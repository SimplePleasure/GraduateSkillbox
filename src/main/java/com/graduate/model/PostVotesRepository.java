package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVotesRepository extends CrudRepository<PostVotes, Integer> {

    @Query(value = "SELECT * FROM blog.post_votes WHERE user_id = :user AND post_id = :post", nativeQuery = true)
    Optional<PostVotes> getVote(@Param("user") int userId, @Param("post") int postId);
}
