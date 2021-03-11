package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.bigtable.BigTable;
import edu.kpi.testcourse.bigtable.DataFolder;
import edu.kpi.testcourse.exception.InvalidUrlException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import javax.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MicronautTest
public class ShortLinkServiceTest {

  @Inject
  private ShortLinkServiceImpl provider;
  @Inject
  private BigTable bigTable;


  private static final String userEmail = "test_user@mail.com";

  @BeforeEach
  public void cleanup() {
    Path path = bigTable.getDir(DataFolder.Links);
    try {
      Files.walk(path).forEach((p) -> {
        try {
          if (!path.equals(p)) {
            Files.delete(p);
          }
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      });
    } catch (IOException exception) {
      exception.printStackTrace();
    }
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
    provider.saveLink(
      "user3@mail.com",
      "https://darcs.realworldhaskell.org/static/00book.pdf",
      "book"
    );

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
    var test1 = new ShortLinkMock(
      "character",
      "user1@mail.com",
      new URL("https://en.wikipedia.org/wiki/Kakashi_Hatake")
    );
    var test2 =new ShortLinkMock(
      linkWithRandomAlias.shortLink(),
      "user1@mail.com",
      new URL("https://github.com/metarhia/metasql")
    );
    ArrayList<ShortLinkMock> resp = provider.getLinksByUserEmail("user1@mail.com");
    assertThat(resp.stream().anyMatch((s) -> s.userEmail().equals(test1.userEmail()))).isTrue();
    assertThat(resp.stream().anyMatch((s) -> s.userEmail().equals(test2.userEmail()))).isTrue();
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
