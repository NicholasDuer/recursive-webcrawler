package webcrawler;

import java.util.Set;

@FunctionalInterface
public interface WebCrawlerAdapter {

  Set<String> requestHrefs(String URL);

}
