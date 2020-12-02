package webcrawler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

public class WebCrawler {

  private final WebCrawlerAdapter adapter;
  
  public WebCrawler(WebCrawlerAdapter adapter) {
    this.adapter = adapter;
  }

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
