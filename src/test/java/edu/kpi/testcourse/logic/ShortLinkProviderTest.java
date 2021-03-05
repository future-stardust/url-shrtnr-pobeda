package edu.kpi.testcourse.logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortLinkProviderTest {

  private static ShortLinkProvider provider;
  private static String userEmail;

  @BeforeAll
  public static void initProvider() {
    userEmail = "test_user@mail.com";
    provider = new ShortLinkProvider();

    provider.saveLink("user3@mail.com", "https://darcs.realworldhaskell.org/static/00book.pdf", "book");
  }

  @Test
  public void shouldProviderExist() {
    ShortLinkProvider shortLinkProvider = new ShortLinkProvider();
    assertThat(shortLinkProvider).isNotNull();
  }

  @Test
  public void shouldGenerateEightCharsAlias() {
    assertThat(provider.generateAlias().length()).isEqualTo(8);
  }

  @Test
  public void shouldContainValidCharacters() {
    String alias = provider.generateAlias();
    assertThat(alias).matches(ShortLinkProvider.ALIAS_PATTERN);
  }

  @Test
  public void shouldNotSaveInvalidUrl() {
    String invalidUrl = "htp//invalid-url.com";

    assertThat(provider.saveLink(userEmail, invalidUrl)).isEmpty();
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

    provider.saveLink(userEmail, url, existentAlias);

    assertThat(provider.getDestinationByShortLink(existentAlias).get().toString())
      .isEqualTo("https://darcs.realworldhaskell.org/static/00book.pdf");
  }

  @Test
  public void shouldNotSaveBadAlias() {
    String badAlias1 = "фф";
    String badAlias2 = "do-not-read";
    String url = "https://ficbook.net/readfic/9255279";

    provider.saveLink(userEmail, url, badAlias1);
    provider.saveLink(userEmail, url, badAlias2);

    assertThat(provider.getDestinationByShortLink(badAlias1)).isEmpty();
    assertThat(provider.getDestinationByShortLink(badAlias2)).isEmpty();
  }
}