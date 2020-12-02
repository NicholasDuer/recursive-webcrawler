package webcrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Adapter used to communicate with the public API exposed by Jsoup.
 */
public class JsoupCrawlerAdapter implements WebCrawlerAdapter {

  /**
   * Uses the Jsoup library to find all hrefs in the page specified by the
   * given URL. Will return absolute links. The search is non-recursive.
   * @param URL URL to the page to be searched.
   * @return A set of hrefs found in the search. If an exception is thrown
   *         when trying to access the URL, the method returns an empty set.
   */
  @Override
  public Set<String> requestHrefs(String URL) {
    try {
      /* Store found hrefs in a LinkedHashSet to maintain order. This makes
       * testing easier. */
      Set<String> hrefs = new LinkedHashSet<>();
      Document doc = Jsoup.connect(URL).get();
      Elements hrefElements = doc.getElementsByAttribute("href");

      for (Element hrefElement : hrefElements) {
        hrefs.add(hrefElement.attr("abs:href"));
      }
      return hrefs;

    } catch (IllegalArgumentException | IOException e) {
      System.out.println("Could not reach the URL: " + URL);

      /* Returns an empty set in the case of failure. */
      return new HashSet<>();
    }
  }
}
