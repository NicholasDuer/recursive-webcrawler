package webcrawler;

import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class WebCrawlerJsoupIntegrationTests {

  WebCrawler webCrawler = new WebCrawler(new JsoupCrawlerAdapter());

  /* These tests are imperfect. Although we test to find URLs which are
   * unlikely to be removed, the tests could still be broken by changes to
   * the pages. However, this is very unlikely and the tests are necessary
   * to verify overall correctness of the system. If there are any failures
   * it would be a good idea to first check the links in the pages still exist.
   */

  @Test
  public void returnsAnHrefWhichWeKnowToBePresent() {
    /* The URL of Wikipedia's page on linux is given as the base root.
     * We know there is a link to Wikipedia's page on Ubuntu in the opening
     * few paragraphs of the document. This URL should be found */

    final String LINUX_URL = "https://en.wikipedia.org/wiki/Linux";
    final String UBUNTU_URL = "https://en.wikipedia.org/wiki/Ubuntu";
    Set<String> hrefs = webCrawler.findHrefs(LINUX_URL, 500);

    Assert.assertTrue(hrefs.contains(UBUNTU_URL));
  }

  @Test
  public void findsHrefsWhichWeKnowToBePresentThroughOneLevelOfRecursion() {
    /* The URL of Wikipedia's page on the results of Ireland's football
     * league 19966 season is given as the base root. We know there is a link
     * to Wikipedia's page on the League of Ireland towards the start of the
     * document. As the link appears early in our search, we expect to
     * recursively search the League of Ireland wikipedia page for hrefs too.
     * Wikipedia's page on the FA of Ireland is linked early on in this page,
     * so we expect to find this href too. */

    final String LEAGUE_OF_IRELAND_RESULTS_URL
        = "https://en.wikipedia.org/wiki/1966%E2%80%9367_League_of_Ireland";
    final String LEAGUE_OF_IRELAND_URL
        = "https://en.wikipedia.org/wiki/League_of_Ireland";
    final String FA_OF_IRELAND
        = "https://en.wikipedia.org/wiki/Football_Association_of_Ireland";

    Set<String> hrefs = webCrawler.findHrefs(LEAGUE_OF_IRELAND_RESULTS_URL, 5000);

    Assert.assertTrue(hrefs.contains(LEAGUE_OF_IRELAND_URL));
    Assert.assertTrue(hrefs.contains(FA_OF_IRELAND));
  }

}
