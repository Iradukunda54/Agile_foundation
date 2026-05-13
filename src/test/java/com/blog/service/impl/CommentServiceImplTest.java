package com.blog.service.impl;

import com.blog.dto.CommentDTO;
import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.User;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
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
class CommentServiceImplTest {

    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User buildAuthor() {
        User author = new User("carol", "pass", "carol@example.com", "USER");
        author.setId(2L);
        return author;
    }

    private Post buildPost(User author) {
        Post post = new Post("My Post", "Post content", author);
        post.setId(5L);
        return post;
    }

    @Test
    void addComment_findsPostAndAuthorThenPersists() {
        User author = buildAuthor();
        Post post = buildPost(author);

        CommentDTO dto = new CommentDTO();
        dto.setContent("Great article!");
        dto.setPostId(5L);
        dto.setAuthorId(2L);

        Comment saved = new Comment("Great article!", post, author);
        saved.setId(100L);

        when(postRepository.findById(5L)).thenReturn(Optional.of(post));
        when(userRepository.findById(2L)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        CommentDTO result = commentService.addComment(dto);

        assertEquals("Great article!", result.getContent());
        assertEquals(5L, result.getPostId());
        assertEquals(2L, result.getAuthorId());
        assertEquals("carol", result.getAuthorName());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_throwsWhenPostNotFound() {
        CommentDTO dto = new CommentDTO();
        dto.setContent("Hello");
        dto.setPostId(99L);
        dto.setAuthorId(2L);

        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.addComment(dto));
        verifyNoInteractions(commentRepository);
    }

    @Test
    void addComment_throwsWhenAuthorNotFound() {
        User author = buildAuthor();
        Post post = buildPost(author);

        CommentDTO dto = new CommentDTO();
        dto.setContent("Hello");
        dto.setPostId(5L);
        dto.setAuthorId(99L);

        when(postRepository.findById(5L)).thenReturn(Optional.of(post));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentService.addComment(dto));
        verifyNoInteractions(commentRepository);
    }

    @Test
    void getCommentsByPost_returnsPageOfCommentDTOs() {
        User author = buildAuthor();
        Post post = buildPost(author);
        Pageable pageable = PageRequest.of(0, 10);

        Comment comment = new Comment("Nice!", post, author);
        comment.setId(1L);
        Page<Comment> page = new PageImpl<>(List.of(comment));

        when(commentRepository.findByPostId(5L, pageable)).thenReturn(page);

        Page<CommentDTO> result = commentService.getCommentsByPost(5L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Nice!", result.getContent().get(0).getContent());
        assertEquals("carol", result.getContent().get(0).getAuthorName());
    }
}
