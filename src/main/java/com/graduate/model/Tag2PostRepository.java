package com.graduate.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {

     /* getTags
        SELECT tp.tag_id, t.name , COUNT(tp.tag_id) as count,
        (SELECT count(*) FROM blog.posts where is_active = 1 and moderation_status = 'ACCEPTED') as total_count,
        (COUNT(tp.tag_id) / (SELECT count(*) FROM blog.posts where is_active = 1 and moderation_status = 'ACCEPTED')) as `weight`
        FROM blog.tag2post tp
        join blog.posts p on tp.post_id = p.id
        join blog.tags t on tp.tag_id = t.id
        where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and t.name like '%'
        group by tp.tag_id order by `weight` DESC
     */

    @Query(value = "SELECT t.name," +
            "(COUNT(tp.tag_id) / (SELECT count(*) FROM blog.posts where is_active = 1 and moderation_status = 'ACCEPTED')) as `weight` " +
            "FROM blog.tag2post tp " +
            "join blog.posts p on tp.post_id = p.id " +
            "join blog.tags t on tp.tag_id = t.id " +
            "where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and t.name like :query " +
            "group by tp.tag_id order by `weight` DESC", nativeQuery = true)
    List<TagWeight> getTags(@Param("query") String query);

    interface TagWeight {
        String getName();
        Double getWeight();

    }
}
