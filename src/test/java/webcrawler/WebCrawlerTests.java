package webcrawler;

import java.util.HashSet;
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

  @Test
  public void stopsSearchingRecursivelyForHrefsWhenLimitReached() {
    /* In this test we set a href limit of 3. URLS 3 and 4 are found
     * from the root search. URL 5 is found from recursively searching URL 3.
     * We should not query URL 4 or 5 as we have now hit our limit. */

    final Integer LIMIT = 3;

    context.checking(new Expectations() {{
      oneOf(adapter).requestHrefs(DUMMY_URL_2);
      will(returnValue(DUMMY_URL_2_HREFS));
      oneOf(adapter).requestHrefs(DUMMY_URL_3);
      will(returnValue(DUMMY_URL_3_HREFS));
      never(adapter).requestHrefs(DUMMY_URL_4);
      never(adapter).requestHrefs(DUMMY_URL_5);
    }});

    Set<String> hrefs = webCrawler.findHrefs(DUMMY_URL_2, LIMIT);

    Set<String> expectedResult = new HashSet<>();
    expectedResult.addAll(DUMMY_URL_2_HREFS);
    expectedResult.addAll(DUMMY_URL_3_HREFS);

    Assert.assertEquals(expectedResult, hrefs);
  }

  @Test
  public void willNotAddHrefsFromAQueryIfLimitWouldBeExceeded() {
    /* In this test we set a href limit of 1. URLS 3 and 4 are found
     * from the root search, but we should only return URL 2.
     * We do not do any more searches. */

    final Integer LIMIT = 1;

    context.checking(new Expectations() {{
      oneOf(adapter).requestHrefs(DUMMY_URL_2);
      will(returnValue(DUMMY_URL_2_HREFS));
      never(adapter).requestHrefs(DUMMY_URL_3);
      never(adapter).requestHrefs(DUMMY_URL_4);
      never(adapter).requestHrefs(DUMMY_URL_5);
    }});

    Set<String> hrefs = webCrawler.findHrefs(DUMMY_URL_2, LIMIT);
    Set<String> expectedResult = new HashSet<>();
    expectedResult.add(DUMMY_URL_3);

    Assert.assertEquals(expectedResult, hrefs);
  }

  @Test
  public void circularInclusionsDoNotResultInInfiniteLoops() {

    final String DUMMY_URL_6 = "https://example6.com/";
    final String DUMMY_URL_7 = "https://example7.com/";

    final Set<String> DUMMY_URL_6_HREFS = new LinkedHashSet<>() {{
      add(DUMMY_URL_7);
    }};

    final Set<String> DUMMY_URL_7_HREFS = new LinkedHashSet<>() {{
      add(DUMMY_URL_6);
    }};

    context.checking(new Expectations() {{
      oneOf(adapter).requestHrefs(DUMMY_URL_6);
      will(returnValue(DUMMY_URL_6_HREFS));
      oneOf(adapter).requestHrefs(DUMMY_URL_7);
      will(returnValue(DUMMY_URL_7_HREFS));
    }});

    Set<String> hrefs = webCrawler.findHrefs(DUMMY_URL_6, Integer.MAX_VALUE);
    Set<String> expectedResult = new HashSet<>();
    expectedResult.addAll(DUMMY_URL_6_HREFS);
    expectedResult.addAll(DUMMY_URL_7_HREFS);

    Assert.assertEquals(expectedResult, hrefs);
  }




}
