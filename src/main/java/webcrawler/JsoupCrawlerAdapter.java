package webcrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawlerAdapter implements WebCrawlerAdapter {

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
