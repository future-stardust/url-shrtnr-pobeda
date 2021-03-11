package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.bigtable.BigTable;
import edu.kpi.testcourse.bigtable.BigTableManager;
import edu.kpi.testcourse.bigtable.BigTableManagerImpl;
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
  private BigTableManagerImpl bigTableManager;

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
