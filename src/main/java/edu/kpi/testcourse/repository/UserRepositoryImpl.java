package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.dto.User;
import java.util.Optional;
import javax.inject.Singleton;

/**
 * Default user repository implementation.
 */
@Singleton
public class UserRepositoryImpl implements UserRepository {

  @Override
  public Optional<User> findByEmail(String email) {
    // TODO: Implement
    return Optional.empty();
  }

  @Override
  public void create(User user) {
    // TODO: implement
  }
}
