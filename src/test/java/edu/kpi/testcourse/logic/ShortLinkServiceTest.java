package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.exception.InvalidUrlException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

  @Test
  public void shouldRetrieveCorrectUrlsByUsersEmail() throws MalformedURLException {
    provider.saveLink(
      "user1@mail.com",
      "https://en.wikipedia.org/wiki/Kakashi_Hatake",
      "character"
    );
    ShortLinkMock linkWithRandomAlias = provider.saveLink(
      "user1@mail.com",
      "https://github.com/metarhia/metasql"
    );
    provider.saveLink(
      "user2@mail.com",
      "https://commons.wikimedia.org/wiki/File:Chestnut-tailed_starling_-_%E0%A6%95%E0%A6%BE%E0%A6%A0_%E0%A6%B6%E0%A6%BE%E0%A6%B2%E0%A6%BF%E0%A6%95.jpg",
      "birds"
    );

    assertThat(provider.getLinksByUserEmail("user1@mail.com")).isEqualTo(
      Arrays.asList(
        new ShortLinkMock(
          "character",
          "user1@mail.com",
          new URL("https://en.wikipedia.org/wiki/Kakashi_Hatake")
        ),
        new ShortLinkMock(
          linkWithRandomAlias.shortLink(),
          "user1@mail.com",
          new URL("https://github.com/metarhia/metasql")
        )
      )
    );
  }

  @Test
  public void shouldBeAbleToDeleteUrl() {
    provider.saveLink(
      "user1@mail.com",
      "https://stackoverflow.com/questions/31079081/programmatically-navigate-using-react-router",
      "sof"
    );
    ShortLinkMock shortLinkRandomAlias = provider.saveLink(
      "user1@mail.com",
      "https://en.wikipedia.org/wiki/Chestnut-tailed_starling"
    );
    provider.saveLink(
      "user2@mail.com",
      "https://en.wikipedia.org/wiki/Sendai",
      "Sendai"
    );

    // delete all urls of user1@mail.com
    provider.deleteLinkIfBelongsToUser("user1@mail.com", "sof");
    provider.deleteLinkIfBelongsToUser("user1@mail.com", shortLinkRandomAlias.shortLink());

    assertThat(provider.getLinksByUserEmail("user1@mail.com")).isEmpty();
  }
}
