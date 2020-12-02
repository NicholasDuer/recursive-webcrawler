# recursive-webcrawler
A webcrawler which starts by going to a page specified by a given URL. It will
then search for URLs of other pages linked to that page. The found URLs will then 
be recursively searched until 100 unique URLs are found in total. 

The repository is written in Java and uses the Jsoup library to navigate web 
pages.


The project is built using Gradle. To build, navigate to the recursive-webcrawler directory.
Then, type the following into your terminal or command prompt: 

```
gradlew build
```

A demo run can then be executed by the command:

```
gradlew run
```

The demo program uses https://news.ycombinator.com as the default
starting URL. To change the starting URL, use the command:

```
gradlew run --args "https://example.com"
```

This webcrawler was written by Nicholas Duer
