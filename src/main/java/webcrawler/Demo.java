package webcrawler;

import java.util.Set;

public class Demo {

  private static final String DEFAULT_ROOT_URL = "https://news.ycombinator.com";
  private static final int LIMIT = 100;

  public static void main(String[] args) {
    WebCrawler webCrawler = new WebCrawler(new JsoupCrawlerAdapter());

    String rootURL;

    if (args.length >= 1) {
      rootURL = args[0];
    } else {
      rootURL = DEFAULT_ROOT_URL;
    }

    System.out.println(
        "Searching for up to " + LIMIT + " URLs starting from: " + rootURL);
    Set<String> foundURLs = webCrawler.findHrefs(rootURL, LIMIT);

    System.out.println("The following URLs were found:");
    for (String URL : foundURLs) {
      System.out.println(URL);
    }
  }
}
