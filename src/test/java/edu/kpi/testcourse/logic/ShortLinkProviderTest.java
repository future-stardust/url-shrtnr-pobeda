package edu.kpi.testcourse.logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortLinkProviderTest {

  private static ShortLinkProvider provider;

  @BeforeAll
  public static void initProvider() {
    provider = new ShortLinkProvider();
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
}
