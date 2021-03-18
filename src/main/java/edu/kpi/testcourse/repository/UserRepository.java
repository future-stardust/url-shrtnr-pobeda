package edu.kpi.testcourse.repository;

import edu.kpi.testcourse.dto.User;
import java.util.Optional;


/**
 * Interface for UserRepository.
 */
public interface UserRepository {

  /**
   * Find user by email.
   *
   * @param email User's email
   * @return Optional with user if he exists, empty Optional otherwise
   */
  Optional<User> findByEmail(String email);

  /**
   * Create user.
   *
   * @param user to create
   */
  void create(User user);
}
