package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.exception.InvalidUrlException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortLinkServiceTest {

  private static ShortLinkServiceImpl provider;
  private static String userEmail;

  @BeforeAll
  public static void initProvider() {
    userEmail = "test_user@mail.com";
    provider = new ShortLinkServiceImpl();

    provider.saveLink(
      "user3@mail.com",
      "https://darcs.realworldhaskell.org/static/00book.pdf",
      "book"
    );
  }

  @Test
  public void shouldProviderExist() {
    ShortLinkServiceImpl shortLinkService = new ShortLinkServiceImpl();
    assertThat(shortLinkService).isNotNull();
  }

  @Test
  public void shouldGenerateEightCharsAlias() {
    assertThat(provider.generateAlias().length()).isEqualTo(8);
  }

  @Test
  public void shouldContainValidCharacters() {
    String alias = provider.generateAlias();
    assertThat(alias).matches(ShortLinkServiceImpl.ALIAS_PATTERN);
  }

  @Test
  public void shouldNotSaveInvalidUrl() {
    String invalidUrl = "htp//invalid-url.com";

    assertThatThrownBy(() -> provider.saveLink(userEmail, invalidUrl))
      .isInstanceOf(InvalidUrlException.class);
  }

  @Test
  public void shouldSaveValidUrl() {
    String validUrl = "https://github.com/nestjs/typeorm";

    assertThat(provider.saveLink(userEmail, validUrl)).isNotNull();
  }

  @Test
  public void shouldNotSaveExistentAlias() {
    String existentAlias = "book";
    String url = "http://genhis.philol.msu.ru/article_35.shtml";

    assertThatThrownBy(() -> provider.saveLink(userEmail, url, existentAlias))
      .isInstanceOf(InvalidUrlException.class);

    assertThat(provider.getDestinationByShortLink(existentAlias).get().toString())
      .isEqualTo("https://darcs.realworldhaskell.org/static/00book.pdf");
  }

  @Test
  public void shouldNotSaveBadAlias() {
    String badAlias1 = "фф";
    String badAlias2 = "do-not-read";
    String url = "https://ficbook.net/readfic/9255279";

    assertThatThrownBy(() -> provider.saveLink(userEmail, url, badAlias1))
      .isInstanceOf(InvalidUrlException.class);
    assertThatThrownBy(() -> provider.saveLink(userEmail, url, badAlias2))
      .isInstanceOf(InvalidUrlException.class);
  }

  @Test
  public void shouldRetrieveSavedLinks() {
    ShortLinkMock shortLink = provider.saveLink(
      "user3@mail.com",
      "https://en.wikipedia.org/wiki/Al-Ahsa_Oasis"
    );

    assertThat(provider.getDestinationByShortLink(shortLink.shortLink()).get())
      .isEqualTo(shortLink.destination());
  }
}
