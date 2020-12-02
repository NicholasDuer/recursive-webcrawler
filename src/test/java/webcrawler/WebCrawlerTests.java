package webcrawler;

import java.util.LinkedHashSet;
import java.util.Set;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

public class WebCrawlerTests {

  public Mockery context = new Mockery();
  WebCrawlerAdapter adapter = context.mock(WebCrawlerAdapter.class);

  WebCrawler webCrawler = new WebCrawler(adapter);

  private static final String DUMMY_URL_1 = "https://example1.com/";
  private static final String DUMMY_URL_2 = "https://example2.com/";
  private static final String DUMMY_URL_3 = "https://example3.com/";
  private static final String DUMMY_URL_4 = "https://example4.com/";
  private static final String DUMMY_URL_5 = "https://example5.com/";

  private static final Set<String> DUMMY_URL_2_HREFS = new LinkedHashSet<>() {{
    add(DUMMY_URL_3);
    add(DUMMY_URL_4);
  }};

  private static final Set<String> DUMMY_URL_3_HREFS = new LinkedHashSet<>() {{
    add(DUMMY_URL_5);
  }};

  @Test
  public void queriesStartingURLForHrefs() {

    context.checking(new Expectations() {{
      oneOf(adapter).requestHrefs(DUMMY_URL_1);
    }});

    webCrawler.findHrefs(DUMMY_URL_1, Integer.MAX_VALUE);
  }

  @Test
  public void recursivelyQueriesURLsForHrefs() {

    context.checking(new Expectations() {{
      oneOf(adapter).requestHrefs(DUMMY_URL_2); will(returnValue(DUMMY_URL_2_HREFS));
      oneOf(adapter).requestHrefs(DUMMY_URL_3); will(returnValue(DUMMY_URL_3_HREFS));
      oneOf(adapter).requestHrefs(DUMMY_URL_4);
      oneOf(adapter).requestHrefs(DUMMY_URL_5);
    }});

    Set<String> hrefs = webCrawler.findHrefs(DUMMY_URL_2, Integer.MAX_VALUE);

    Assert.assertTrue(hrefs.containsAll(DUMMY_URL_2_HREFS));
    Assert.assertTrue(hrefs.containsAll(DUMMY_URL_3_HREFS));

  }



}
