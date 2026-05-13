package com.blog.service.impl;

import com.blog.dto.UserDTO;
import com.blog.model.User;
import com.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser(Long id) {
        User user = new User("alice", "secret", "alice@example.com", "USER");
        user.setId(id);
        return user;
    }

    @Test
    void createUser_savesUserAndReturnsMappedDTO() {
        UserDTO dto = new UserDTO();
        dto.setUsername("alice");
        dto.setPassword("secret");
        dto.setEmail("alice@example.com");
        dto.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(buildUser(1L));

        UserDTO result = userService.createUser(dto);

        assertEquals("alice", result.getUsername());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("USER", result.getRole());
        assertNull(result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_returnsUserDTOWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(buildUser(1L)));

        UserDTO result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals("alice", result.getUsername());
    }

    @Test
    void getUserById_throwsWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void getAllUsers_returnsListOfDTOs() {
        when(userRepository.findAll()).thenReturn(List.of(buildUser(1L), buildUser(2L)));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void updateUser_updatesFieldsAndReturnsSavedDTO() {
        User existing = buildUser(1L);
        UserDTO dto = new UserDTO();
        dto.setUsername("alice_updated");
        dto.setEmail("updated@example.com");
        dto.setRole("ADMIN");

        User updated = new User("alice_updated", "secret", "updated@example.com", "ADMIN");
        updated.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        UserDTO result = userService.updateUser(1L, dto);

        assertEquals("alice_updated", result.getUsername());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void deleteUser_callsRepositoryDeleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}
