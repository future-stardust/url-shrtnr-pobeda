package edu.kpi.testcourse.auth;

import edu.kpi.testcourse.dto.User;
import edu.kpi.testcourse.exception.user.UserNotFoundException;
import edu.kpi.testcourse.repository.UserRepository;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.Email;
import org.reactivestreams.Publisher;

/**
 * JWT-based authentication provider logic: ensures that a user is
 * registered in the system and password is right.
 */
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  @Inject
  private UserRepository userRepository;

  /**
   * Authentication method that provides validation of entered credentials.
   *
   * @param httpRequest HTTP request reference
   * @param authenticationRequest Authentication credentials to validate
   *
   * @return JWT or authentication exception
   */
  @Override
  public Publisher<AuthenticationResponse> authenticate(
      @Nullable HttpRequest<?> httpRequest,
      AuthenticationRequest<?, ?> authenticationRequest
  ) {
    final @Email String entryEmail = authenticationRequest.getIdentity().toString();
    final String entryPassword = authenticationRequest.getSecret().toString();

    return Flowable.create(emitter -> {
      try {
        final Optional<User> user = userRepository.findByEmail(entryEmail);
        if (user.isPresent() && Objects.equals(entryPassword, user.get().password())) {
          final UserDetails userDetails = new UserDetails(entryEmail,
            new ArrayList<>(Collections.singletonList("USER")));
          emitter.onNext(userDetails);
        } else {
          emitter.onError(new AuthenticationException(
            new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)));
        }
      } catch (UserNotFoundException exc) {
        emitter.onError(new AuthenticationException(
          new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)));
      }
      emitter.onComplete();
    }, BackpressureStrategy.ERROR);
  }
}
