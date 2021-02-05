package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    // TODO: 10.01.2021  add select criteria is_active = 1 && moderation_status = ACCEPTED

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

//  todo: add comments rating to general rating
    @Query(value = "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.text, p.time, p.title, p.view_count, p.user_id " +
            "FROM blog.posts p LEFT JOIN (" +
            "SELECT pv.post_id, SUM(CASE WHEN pv.value = -1 THEN -2 ELSE 5 END) AS `RATING` FROM blog.post_votes pv " +
            "GROUP BY pv.post_id ) v ON v.post_id = p.id " +
            "ORDER BY COALESCE(v.RATING, 0) DESC " +
            "LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findBestPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit);
/*
    "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.text, p.time, p.title, p.view_count, p.user_id
    FROM blog.posts p LEFT JOIN (
    SELECT pv.post_id, SUM(CASE WHEN pv.value = -1 THEN -2 ELSE 5 END) AS `RATING` FROM blog.post_votes pv
    GROUP BY pv.post_id ) v ON v.post_id = p.id
    ORDER BY COALESCE(v.RATING, 0) DESC
    LIMIT :offset, :limit"
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



    // select by tag
    @Query(value = "SELECT count(*) FROM blog.posts p LEFT JOIN blog.tag2post tp ON p.id = tp.post_id " +
            "LEFT JOIN blog.tags t ON tp.tag_id = t.id WHERE t.name LIKE :tag", nativeQuery = true)
    int getPostsCountByTag(@Param(value = "tag") String tag);

    @Query(value = "SELECT p.id, p.is_active, p.moderation_status, p.moderator_id, p.`text`, p.`time`, p.title, " +
            "p.view_count, p.user_id FROM blog.posts p JOIN blog.tag2post tp ON p.id = tp.post_id JOIN blog.tags t " +
            "ON tp.tag_id = t.id WHERE t.name LIKE :tag LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findPostsByTag(@Param(value = "offset") int offset,
                              @Param(value = "limit") int limit, @Param(value = "tag") String tag);






//      TODO: 18.01.2021 Method don't finish. Required auth.
//      TODO: 16.01.2021 Посты со статусами accepted и declined должны выводится в соответствии с id модератора
    @Query(value = "SELECT COUNT(*) FROM blog.posts WHERE is_active = 1 AND moderation_status LIKE :status AND " +
            "(moderator_id = :moderatorId OR moderator_id IS NULL)", nativeQuery = true)
    int getPostsCountByModerationStatus(@Param(value = "status") String status, @Param(value = "moderatorId") int moderatorId);

    @Query(value = "SELECT * FROM blog.posts WHERE is_active = 1 AND moderation_status LIKE :status " +
            "AND (moderator_id = :id OR moderator_id IS NULL) LIMIT :offset,:limit", nativeQuery = true)
    List<Post> findPostsByModerationStatus(@Param(value = "offset") int offset, @Param(value = "limit") int limit,
                                           @Param(value = "status") String status, @Param(value = "id") int moderatorId);


//      TODO: 18.01.2021 Method don't finish. Required auth.
    @Query(value = "SELECT COUNT(*) FROM blog.posts p " +
            "WHERE user_id = :id AND is_active = :isActive AND moderation_status = :status", nativeQuery = true)
    int getMyPostsCount(@Param(value = "id") int userId, @Param(value = "isActive") int isActive,
                        @Param(value = "status") String status);

    @Query(value = "SELECT * FROM blog.posts p WHERE user_id = :id AND is_active = :isActive AND moderation_status = :status LIMIT :offset, :limit", nativeQuery = true)
    List<Post> findMyPosts(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "id") int userId,
                           @Param(value = "isActive") int isActive, @Param(value = "status") String status);





    @Query(value = "SELECT YEAR(`time`) as `year` FROM blog.posts p GROUP BY `year`", nativeQuery = true)
    List<Integer> getYearsWherePostExists();
    @Query(value = "SELECT DATE(`time`) AS `date`, COUNT(*) AS `count` FROM blog.posts " +
            "WHERE YEAR(`time`) = :year GROUP BY `date` ORDER BY `date`", nativeQuery = true)
    List<PostsCountByDate> getPostsCountGroupByDays(@Param(value = "year") int year);

    interface PostsCountByDate{
        LocalDate getDate();
        int getCount();
    }
}
