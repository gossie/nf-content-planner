package com.github.gossie.nf.planner.user;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultUserServiceTest {

    @Test
    void shouldCreateUser() {
        var user = new User(null, "test@email.de", "Max", "Mustermann", "123456a", null, List.of());
        var createdUser = new User("4711", "test@email.de", "Max", "Mustermann", "123456a", null, List.of());

        var userRepository = mock(UserRepository.class);
        when(userRepository.createUser(user)).thenReturn(createdUser);

        var userService = new DefaultUserService(userRepository);

        assertThat(userService.createUser(user)).isSameAs(createdUser);
    }

    @Test
    void shouldCreateGitHubUser() {
        var user = new User(null, "test@email.de", "Max", "Mustermann", null, "1234", List.of());
        var createdUser = new User("4711", "test@email.de", "Max", "Mustermann", null, "1234", List.of());

        var userRepository = mock(UserRepository.class);
        when(userRepository.createUser(user)).thenReturn(createdUser);

        var userService = new DefaultUserService(userRepository);

        assertThat(userService.createUser(user)).isSameAs(createdUser);
    }

    @Test
    void shouldNotCreateUserEmailAlreadyExists() {
        var user = new User(null, "test@email.de", "Max", "Mustermann", "123456a", null, List.of());
        var savedUser = new User("4711", "test@email.de", "Max", "Mustermann", "123456a", null, List.of());

        var userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail("test@email.de")).thenReturn(Optional.of(savedUser));

        var userService = new DefaultUserService(userRepository);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> userService.createUser(user));
    }

    @Test
    void shouldCreateGitHubUserWithEmptyEmail() {
        var user = new User(null, null, "Max", "Mustermann", null, "1234", List.of());
        var otherGitHubUser = new User("0815", null, "Another", "User", null, "9876", List.of());
        var createdUser = new User("4711", null, "Max", "Mustermann", null, "1234", List.of());

        var userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(null)).thenReturn(Optional.of(otherGitHubUser));
        when(userRepository.createUser(user)).thenReturn(createdUser);

        var userService = new DefaultUserService(userRepository);

        assertThat(userService.createUser(user)).isSameAs(createdUser);
    }

    @Test
    void shouldMergeUsers() {
        var user = new User(null, "test@email.de", "Max", "Mustermann", null, "1234", List.of());
        var savedUser = new User("4711", "test@email.de", "Max", "Mustermann", "123456a", null, List.of());
        var mergedUser = new User("4711", "test@email.de", "Max", "Mustermann", "123456a", "1234", List.of());
        var createdUser = new User("4711", "test@email.de", "Max", "Mustermann", "123456a", "1234", List.of());

        var userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail("test@email.de")).thenReturn(Optional.of(savedUser));
        when(userRepository.saveUser(mergedUser)).thenReturn(createdUser);

        var userService = new DefaultUserService(userRepository);

        assertThat(userService.createUser(user)).isSameAs(createdUser);
    }

}