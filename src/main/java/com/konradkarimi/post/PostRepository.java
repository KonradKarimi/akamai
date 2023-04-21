package com.konradkarimi.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<SocialNetworkPost, Long> {


    /**
     * @return returns the top 10 most viewed posts based on the viewCount field.
     */
    @Query("SELECT p FROM SocialNetworkPost p ORDER BY p.viewCount DESC limit 10")
    List<SocialNetworkPost> findTopTenViewedPosts();
}
