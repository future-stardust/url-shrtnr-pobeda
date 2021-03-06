package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.bigtable.BigTableException;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Default user repository implementation.
 */
@Singleton
public class UserRepositoryImpl implements UserRepository {

  @Inject
  private BigTableManager bigTableManager;

  @Override
  public Optional<User> findByEmail(String email) {
    try {
      return bigTableManager.findEmail(email);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }

  @Override
  public void create(User user) {
    try {
      bigTableManager.storeUser(user);
    } catch (IOException exception) {
      throw new BigTableException(exception);
    }
  }
}
