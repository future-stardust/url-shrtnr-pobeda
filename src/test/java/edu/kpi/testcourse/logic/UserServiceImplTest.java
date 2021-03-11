package edu.kpi.testcourse.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.user.InvalidSignUpRequestException;
import edu.kpi.testcourse.exception.user.UserAlreadyExistsException;
import edu.kpi.testcourse.repository.UserRepository;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.util.Optional;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@MicronautTest
class UserServiceImplTest {

  @Inject
  UserService userService;

  @MockBean(UserRepository.class)
  public UserRepository userRepository() {
    UserRepository userRepository = mock(UserRepository.class);
    Mockito
      .when(userRepository.findByEmail("existent@email"))
      .thenReturn(Optional.of(new User("existent@email", "pass")));
    return userRepository;
  }

  @Test
  public void whenInvalidRequest_thenThrowException() {
    User invalidUser = new User("", "");

    assertThrows(InvalidSignUpRequestException.class, () -> userService.signup(invalidUser));
  }

  @Test
  public void whenExistentUserSignUpRequest_thenThrowException() {
    User existentUser = new User("existent@email", "password");

    assertThrows(UserAlreadyExistsException.class, () -> userService.signup(existentUser));
  }

  @Test
  public void whenValidRequest_thenReturnNothing() {
    User validUser = new User("test@com", "password");

    assertDoesNotThrow(() -> userService.signup(validUser));
  }
}
