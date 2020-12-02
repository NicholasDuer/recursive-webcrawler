package webcrawler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Webcrawler takes an adapter parameter in its constructor. This adapter will
 * be used to communicate with suitable third party software to recursively
 * search for hrefs.
 */
public class WebCrawler {

  private final WebCrawlerAdapter adapter;

  /**
   * Constructs a new WebCrawler which uses the supplied adapter to communicate
   * with third party libraries.
   * @param adapter Adapter object which implements 'WebCrawlerAdapter'. This
   *                will be used to communicate with third party libraries to
   *                search for URLs.
   */
  public WebCrawler(WebCrawlerAdapter adapter) {
    this.adapter = adapter;
  }

  /**
   * Will search for URLs in the page specified by the rootURL. Once this search
   * is complete, will then recursively search the found URLs to find even
   * more URLs. Will continue searching in this fashion until a limit is
   * hit.
   * @param rootURL Specifies the starting URL of the search.
   * @param limit The maximum number of URLs which should be returned.
   * @return A set of unique URLs found in the search.
   */
  public Set<String> findHrefs(String rootURL, Integer limit) {

    /* Store found hrefs in a LinkedHashSet to maintain order of insertion
     * and make testing easier, as behaviour is now predictable and easier
     * to reason about. */
    Set<String> hrefs = new LinkedHashSet<>();

    Set<String> visited = new HashSet<>();

    /* Queue of URLs to be should queried for hrefs. This webcrawler finds hrefs
     * in a breadth first search fashion. */
    Queue<String> queryQueue = new ArrayDeque<>();
    queryQueue.add(rootURL);

    while (!queryQueue.isEmpty() && hrefs.size() < limit) {
      String nextQuery = queryQueue.poll();

      /* Only load URL if we have not visited it previously. */
      if (!visited.contains(nextQuery)) {
        Set<String> queryResult = adapter.requestHrefs(nextQuery);

        for (String href : queryResult) {
          if (hrefs.size() < limit) {
            hrefs.add(href);
            queryQueue.add(href);
          } else {
            break;
          }
        }
        visited.add(nextQuery);
      }
    }

    return hrefs;
  }
}
