/**
 * This class is responsible for handling all the business logic of the application.
 * It uses PostRepository to handle all the database operations.
 */

package com.konradkarimi.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public SocialNetworkPost createPost(String author, String content) {
        SocialNetworkPost post = new SocialNetworkPost();
        post.setAuthor(author);
        post.setContent(content);
        post.setPostDate(new Date());
        post.setViewCount(0L);
        return post;
    }

    /**
     * @return page of sized 50 posts from the database.
     */
    public List<SocialNetworkPost> getAllPosts() {
        Page<SocialNetworkPost> page = postRepository.findAll(Pageable.ofSize(50));
        return page.getContent();
    }

    /**
     * @param id of the post to be found.
     * @return post with the given id.
     */
    public SocialNetworkPost findById(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    /**
     * @param post to be added to the database.
     */
    public void addPost(SocialNetworkPost post) {
        postRepository.save(post);
    }

    /**
     * @param id of the post to be deleted.
     *           If the post does not exist, it throws an IllegalStateException.
     */
    public void deletePost(Long id) {
        boolean postExists = postRepository.existsById(id);
        if (!postExists) {
            throw new IllegalStateException("Post with id " + id + " does not exist.");
        }
        postRepository.deleteById(id);
    }

    /**
     * @param post to be updated.
     */
    public void updatePost(SocialNetworkPost post) {
        postRepository.save(post);
    }

    /**
     * @return list of top ten viewed posts based on the viewCount field.
     */
    public List<SocialNetworkPost> getTopTenViewedPosts() {
        return postRepository.findTopTenViewedPosts();
    }


}
