package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    // TODO: 10.01.2021  add select criteria is_active = 1 && moderation_status = ACCEPTED
    // Is the query methods require return LinkedList for saving exact order or hibernate can create arraylist with capacity?

    @Query(value = "SELECT COUNT(*) FROM blog.posts ", nativeQuery = true)
    int getPostsCountByMode();

    @Query(value = "SELECT * FROM blog.posts ORDER BY `time` DESC LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findRecentPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit);

    @Query(value = "SELECT * FROM blog.posts ORDER BY `time` ASC LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findEarlyPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit);

    @Query(value = "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.user_id, p.`time`, p.title, " +
            "p.`text`, p.view_count FROM blog.posts p LEFT JOIN blog.post_comments pc ON p.id = pc.post_id GROUP BY " +
            "p.id ORDER BY COUNT(pc.`text`) DESC LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findPopularPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit);

//  todo: неправильно выводится колличество постов(посты только с дизлайками выводится не будут) Следовательно
//        getPostsCountByMode() может выдавать неправильный результат
    @Query(value = "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.text, p.time, p.title, " +
            "p.view_count, p.user_id FROM blog.posts p LEFT JOIN blog.post_votes pv ON p.id = pv.post_id " +
            "WHERE pv.value = 1 OR pv.value IS NULL GROUP BY p.id ORDER BY SUM(pv.value) DESC LIMIT :offset, :limit",
            nativeQuery = true)
    List<Post> findBestPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit);

/*
        Not correct receiving posts by like count (sort by votes sum)
    SELECT p.id, sum(pv.value)
    FROM blog.posts p
    left join blog.post_votes pv on p.id = pv.post_id
    group by p.id
    order by sum(pv.value) DESC

        This query doesn't include posts without votes
    SELECT p.id, sum(pv.value)
    FROM blog.posts p
    left join blog.post_votes pv on p.id = pv.post_id WHERE pv.value != -1
    group by p.id
    order by sum(pv.value) DESC


        this approach ignore posts without positive values
    SELECT p.id, p.text, SUM(pv.value)
    FROM blog.posts p
    LEFT JOIN blog.post_votes pv ON p.id = pv.post_id
    WHERE pv.value = 1 OR pv.value IS NULL
    GROUP BY p.id
    ORDER BY SUM(pv.value) DESC
    LIMIT 0, 10

 */

    //select by search
    @Query(value = "SELECT COUNT(*) FROM blog.posts p WHERE `text` LIKE :query", nativeQuery = true)
    int getPostsCountBySearchQuery(@Param(value = "query") String searchQuery);

    @Query(value = "SELECT * FROM blog.posts WHERE `text` LIKE :query LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findPostsBySearchQuery(@Param(value = "offset") int offset,
                                      @Param(value = "limit") int limit, @Param(value = "query") String searchQuery);


    //select by date
    @Query(value = "SELECT COUNT(*) FROM blog.posts p WHERE `time` LIKE :date", nativeQuery = true)
    int getPostsCountByDate(@Param(value = "date") String date);

    @Query(value = "SELECT * FROM blog.posts p WHERE `time` LIKE :date LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findPostsByDate(@Param(value = "offset") int offset,
                               @Param(value = "limit") int limit, @Param(value = "date") String date);






    // TODO: 18.01.2021 return empty list!!!!!!?
    // select by tag
    @Query(value = "SELECT count(*) FROM blog.posts p LEFT JOIN blog.tag2post tp ON p.id = tp.post_id " +
            "LEFT JOIN blog.tags t ON tp.tag_id = t.id WHERE t.name LIKE :tag", nativeQuery = true)
    int getPostsCountByTag(@Param(value = "tag") String tag);

    @Query(value = "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.`text`, p.`time`, p.title, " +
            "p.view_count, p.user_id FROM blog.posts p JOIN blog.tag2post tp ON p.id = tp.post_id JOIN blog.tags t " +
            "ON tp.tag_id = t.id WHERE t.name LIKE :tag LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findPostsByTag(@Param(value = "offset") int offset,
                              @Param(value = "limit") int limit, @Param(value = "tag") String tag);






//      TODO: 18.01.2021 return empty list!!!!!!?
//      TODO: 16.01.2021 Посты со статусами accepted и declined должны выводится в соответствии с id модератора
//    @Query(value = "SELECT COUNT(*) FROM blog.posts WHERE moderation_status LIKE 'NEW' AND is_active = 1", nativeQuery = true)
//    int getPostsCountWithModerationStatusNew();

    @Query(value = "SELECT COUNT(*) FROM blog.posts WHERE is_active = 1 AND moderation_status LIKE :status AND " +
            "(moderator_id = :moderatorId OR moderator_id IS NULL)", nativeQuery = true)
    int getPostsCountByModerationStatus(@Param(value = "status") String status, @Param(value = "moderatorId") int moderatorId);

    @Query(value = "SELECT * FROM blog.posts WHERE is_active = 1 AND moderation_status LIKE :status " +
            "AND (moderator_id = :id OR moderator_id IS NULL) LIMIT :offset,:limit", nativeQuery = true)
    List<Post> findPostsByModerationStatus(@Param(value = "offset") int offset, @Param(value = "limit") int limit,
                                           @Param(value = "status") String status, @Param(value = "id") int moderatorId);







    @Query(value = "SELECT COUNT(*) FROM blog.posts p " +
            "WHERE user_id = :id AND is_active = :isActive AND moderation_status = :status", nativeQuery = true)
    int getMyPostsCount(@Param(value = "id") int userId, @Param(value = "isActive") int isActive,
                        @Param(value = "status") String status);

    @Query(value = "SELECT * FROM blog.posts p WHERE user_id = :id AND is_active = :isActive AND moderation_status = :status LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findMyPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "id") int userId,
                           @Param(value = "isActive") int isActive, @Param(value = "status") String status);

}
