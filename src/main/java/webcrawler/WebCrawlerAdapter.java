package webcrawler;

import java.util.Set;

/**
 * Interface which specifies an adapter used to communicate with third party
 * API.
 */
@FunctionalInterface
public interface WebCrawlerAdapter {

  /**
   * Uses third party API to search for hrefs in the page specified by the
   * given URL. The search is non-recursive.
   * @param URL URL to the page to be searched.
   * @return A set of href strings found in the search.
   */
  Set<String> requestHrefs(String URL);

}
