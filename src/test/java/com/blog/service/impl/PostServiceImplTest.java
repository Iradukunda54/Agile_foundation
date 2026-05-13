package com.blog.service.impl;

import com.blog.dto.PostDTO;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private TagRepository tagRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private User buildAuthor() {
        User author = new User("bob", "pass", "bob@example.com", "USER");
        author.setId(1L);
        return author;
    }

    private Post buildPost(User author) {
        Post post = new Post("Spring Tips", "Great content here", author);
        post.setId(10L);
        return post;
    }

    @Test
    void createPost_findsAuthorAndPersistsPost() {
        User author = buildAuthor();
        PostDTO dto = new PostDTO();
        dto.setTitle("Spring Tips");
        dto.setContent("Great content here");
        dto.setAuthorId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(postRepository.save(any(Post.class))).thenReturn(buildPost(author));

        PostDTO result = postService.createPost(dto);

        assertEquals("Spring Tips", result.getTitle());
        assertEquals(1L, result.getAuthorId());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_throwsWhenAuthorNotFound() {
        PostDTO dto = new PostDTO();
        dto.setAuthorId(99L);
        dto.setTitle("T");
        dto.setContent("C");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.createPost(dto));
    }

    @Test
    void getPostById_returnsPostAndIncrementsViews() {
        User author = buildAuthor();
        Post post = buildPost(author);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDTO result = postService.getPostById(10L);

        assertEquals("Spring Tips", result.getTitle());
        assertEquals(1, post.getViews());
    }

    @Test
    void getPostById_throwsWhenNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.getPostById(99L));
    }

    @Test
    void getAllPosts_returnsPageOfDTOs() {
        User author = buildAuthor();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> page = new PageImpl<>(List.of(buildPost(author)));

        when(postRepository.findAll(pageable)).thenReturn(page);

        Page<PostDTO> result = postService.getAllPosts(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Spring Tips", result.getContent().get(0).getTitle());
    }

    @Test
    void searchPosts_returnsMatchingPosts() {
        User author = buildAuthor();
        when(postRepository.searchByKeyword("spring")).thenReturn(List.of(buildPost(author)));

        List<PostDTO> result = postService.searchPosts("spring");

        assertEquals(1, result.size());
        assertEquals("Spring Tips", result.get(0).getTitle());
    }

    @Test
    void updatePost_updatesFieldsAndReturnsDTO() {
        User author = buildAuthor();
        Post existing = buildPost(author);
        PostDTO dto = new PostDTO();
        dto.setTitle("Updated Title");
        dto.setContent("Updated content");

        Post updated = new Post("Updated Title", "Updated content", author);
        updated.setId(10L);

        when(postRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenReturn(updated);

        PostDTO result = postService.updatePost(10L, dto);

        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void deletePost_delegatesToRepository() {
        doNothing().when(postRepository).deleteById(10L);

        postService.deletePost(10L);

        verify(postRepository).deleteById(10L);
    }
}
