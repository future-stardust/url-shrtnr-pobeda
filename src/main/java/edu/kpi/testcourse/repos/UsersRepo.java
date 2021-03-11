package edu.kpi.testcourse.repos;

import edu.kpi.testcourse.exception.UsersException;
import java.util.List;
import java.util.Optional;

/**
 * Interface for managing users, validating and so on.
 */
public interface UsersRepo {

  /**
   * Creates new storage unit for new user via BigTable.
   *
   * @param email users identifier
   * @param password users pass
   * @throws UsersException if trying to create new user with existing identifier
   */
  void storeNewUser(String email, String password) throws UsersException;

  /**
   * Checks if provided password are correct.
   *
   * @param email users identifier
   * @param possiblePassword provided password
   * @return true if check passed, false if failed
   */
  boolean verifyUsersPassword(String email, String possiblePassword);

  /**
   * Finds all aliases stored under certain user.
   *
   * @param email users identifier
   * @return list of aliases
   * @throws UsersException if user with provided email does not exist
   */
  Optional<List<String>> getUsersAliases(String email) throws UsersException;

}
