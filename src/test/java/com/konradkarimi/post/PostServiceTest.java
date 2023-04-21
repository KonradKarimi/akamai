/**
 * This class contains unit tests for PostService class.
 */

package com.konradkarimi.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository);
    }

    @Test
    void createPost_shouldCreateNewPostWithAuthorAndContent() {
        String author = "Konrad";
        String content = "This is a test post.";
        SocialNetworkPost post = postService.createPost(author, content);

        assertEquals(author, post.getAuthor());
        assertEquals(content, post.getContent());
        assertEquals(0L, post.getViewCount());
        Assertions.assertNotNull(post.getPostDate());
    }

    @Test
    void getAllPosts_shouldReturnListOfPosts() {
        List<SocialNetworkPost> expectedPosts = new ArrayList<>();
        expectedPosts.add(new SocialNetworkPost(1L, new Date(), "Author 1", "Content 1", 0L));
        expectedPosts.add(new SocialNetworkPost(2L, new Date(), "Author 2", "Content 2", 0L));

        Page<SocialNetworkPost> page = new PageImpl<>(expectedPosts);

        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<SocialNetworkPost> actualPosts = postService.getAllPosts();

        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts, actualPosts);

        verify(postRepository, times(1)).findAll(any(Pageable.class));


    }


    @Test
    void findById_shouldReturnPostWithGivenId() {
        Long postId = 1L;
        SocialNetworkPost expectedPost = new SocialNetworkPost(postId, new Date(), "Author", "Content", 0L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));
        SocialNetworkPost actualPost = postService.findById(postId);

        assertEquals(expectedPost, actualPost);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void addPost_shouldSavePostToRepository() {
        SocialNetworkPost post = new SocialNetworkPost(1L, new Date(), "Author", "Content", 0L);
        postService.addPost(post);

        verify(postRepository, times(1)).save(post);

    }

    @Test
    void deletePost_shouldDeletePostFromRepository() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(true);
        postService.deletePost(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void deletePost_shouldThrowExceptionWhenPostDoesNotExist() {
        Long postId = 1L;

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            postService.deletePost(postId);
        });

        verify(postRepository, never()).deleteById(postId);
    }

    @Test
    void updatePost_shouldSaveUpdatedPostToRepository() {
        SocialNetworkPost post = new SocialNetworkPost(1L, new Date(), "Author", "Content", 0L);
        post.setContent("Updated content");
        postService.updatePost(post);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllPosts() {
        // Mocking repository behavior
        Page<SocialNetworkPost> mockPage = Mockito.mock(Page.class);
        List<SocialNetworkPost> mockPosts = new ArrayList<>();
        mockPosts.add(new SocialNetworkPost(1L, new Date(), "author1", "content1", 0L));
        mockPosts.add(new SocialNetworkPost(2L, new Date(), "author2", "content2", 0L));
        Mockito.when(mockPage.getContent()).thenReturn(mockPosts);
        Mockito.when(postRepository.findAll(Pageable.ofSize(50))).thenReturn(mockPage);

        // Testing service method
        List<SocialNetworkPost> result = postService.getAllPosts();

        // Assertions
        assertEquals(mockPosts, result);
        Mockito.verify(postRepository, Mockito.times(1)).findAll(Pageable.ofSize(50));
    }

    @Test
    void testFindById() {
        // Mocking repository behavior
        SocialNetworkPost mockPost = new SocialNetworkPost(1L, new Date(), "author1", "content1", 0L);
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        // Testing service method
        SocialNetworkPost result = postService.findById(1L);

        // Assertions
        assertEquals(mockPost, result);
        Mockito.verify(postRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testAddPost() {
        // Mocking repository behavior
        SocialNetworkPost mockPost = new SocialNetworkPost(1L, new Date(), "author1", "content1", 0L);

        // Testing service method
        postService.addPost(mockPost);

        // Assertions
        Mockito.verify(postRepository, Mockito.times(1)).save(mockPost);
    }

    @Test
    void testDeletePost() {
        // Mocking repository behavior
        Mockito.when(postRepository.existsById(1L)).thenReturn(true);

        // Testing service method
        postService.deletePost(1L);

        // Assertions
        Mockito.verify(postRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeletePostNonExistent() {
        // Mocking repository behavior
        Mockito.when(postRepository.existsById(1L)).thenReturn(false);

        // Testing service method
        assertThrows(IllegalStateException.class, () -> postService.deletePost(1L));

        // Assertions
        Mockito.verify(postRepository, Mockito.times(0)).deleteById(1L);
    }

    @Test
    void testUpdatePost() {
        // Mocking repository behavior
        SocialNetworkPost mockPost = new SocialNetworkPost(1L, new Date(), "author1", "content1", 0L);

        // Testing service method
        postService.updatePost(mockPost);

        // Assertions
        Mockito.verify(postRepository, Mockito.times(1)).save(mockPost);
    }

    @Test
    void testGetTopTenViewedPosts() {
        List<SocialNetworkPost> expectedPosts = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            expectedPosts.add(new SocialNetworkPost((long) i, new Date(), "Author " + i, "Content " + i, (long) i));
        }

        when(postRepository.findTopTenViewedPosts()).thenReturn(expectedPosts);
        List<SocialNetworkPost> actualPosts = postService.getTopTenViewedPosts();

        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts, actualPosts);

        verify(postRepository, times(1)).findTopTenViewedPosts();
    }

}
