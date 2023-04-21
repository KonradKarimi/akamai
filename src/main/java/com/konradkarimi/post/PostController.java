/**
 * This is a controller class. It is responsible for handling all the requests from the client.
 * It is also responsible for returning the response to the client.
 * Just ordinary REST controller.
 * It uses PostService to handle all the business logic.
 */

package com.konradkarimi.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {


    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * @return page of sized 50 posts from the database.
     */
    @GetMapping
    public ResponseEntity<List<SocialNetworkPost>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * @param id of the post to be found.
     * @return post with the given id.
     */
    @GetMapping(path = "{id}")
    public ResponseEntity<SocialNetworkPost> getPost(@PathVariable Long id) {
        SocialNetworkPost post = postService.findById(id);
        post.setViewCount(post.getViewCount() + 1);
        postService.updatePost(post);
        return ResponseEntity.ok(post);
    }


    /**
     * @param request with the author and content of the post to be created.
     * @return created post.
     */
    @PostMapping
    public ResponseEntity<SocialNetworkPost> addPost(@RequestBody NewPostRequest request) {
        SocialNetworkPost post = postService.createPost(
                request.author,
                request.content
        );
        postService.addPost(post);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + post.getId())).body(post);
    }

    /**
     * @param id of the post to be deleted.
     * @return no content.
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param id      of the post to be updated.
     * @param request with the author and content of the post to be updated.
     * @return updated post.
     */
    @PutMapping(path = "{id}")
    public ResponseEntity<SocialNetworkPost> updatePost(@PathVariable Long id, @RequestBody NewPostRequest request) {
        SocialNetworkPost post = postService.findById(id);
        post.setAuthor(request.author);
        post.setContent(request.content);
        postService.updatePost(post);
        return ResponseEntity.ok(post);
    }


    /**
     * @return top ten most viewed posts.
     */
    @GetMapping(path = "/top-ten")
    public ResponseEntity<List<SocialNetworkPost>> getTopTenViewedPosts() {
        return ResponseEntity.ok(postService.getTopTenViewedPosts());
    }

    /**
     * This method is used to generate random posts.
     * WARN: It was used to quickly generate some data for the application.
     */
    @PostMapping(path = "/generate")
    public void generateRandomPosts() {
        for (int i = 0; i < 100; i++) {
            SocialNetworkPost post = postService.createPost(
                    "Konrad",
                    "This is a random post number " + i + "."
            );
            post.setViewCount((long) (Math.random() * 1000));
            postService.addPost(post);
        }
    }

    record NewPostRequest(String author, String content) {
    }
}
