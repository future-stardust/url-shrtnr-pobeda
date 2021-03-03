package edu.kpi.testcourse.logic;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import javax.inject.Singleton;

record ShortLinkMock (
  String shortLink,
  String userEmail,
  URL destination
) {}

@Singleton
public class ShortLinkProvider {
  private ArrayList<ShortLinkMock> mockData;

  {
    try {
      mockData = new ArrayList<ShortLinkMock>(
        Arrays.asList(
          new ShortLinkMock(
            "t5H9678F",
            "user1@mail.com",
            new URL("https://www.instagram.com/")
          ),
          new ShortLinkMock(
            "f7G91057",
            "user1@mail.com",
            new URL("https://texty.org.ua")
          ),
          new ShortLinkMock(
            "mpP76027",
            "user2@mail.com",
            new URL("https://uk.wikipedia.org/wiki/Ніїґата")
          ),
          new ShortLinkMock(
            "manga-character",
            "user1@mail.com",
            new URL("https://readmanga.live/klinok__rassekaiuchii_demonov/vol23/206#page=4")
          ),
          new ShortLinkMock(
            "19l9fr23",
            "user3@mail.com",
            new URL("https://darcs.realworldhaskell.org/static/00book.pdf")
          )
        )
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
}
