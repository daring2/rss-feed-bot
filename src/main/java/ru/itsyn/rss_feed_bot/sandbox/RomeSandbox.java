package ru.itsyn.rss_feed_bot.sandbox;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;

public class RomeSandbox {

    public static void main(String[] args) throws Exception {
        var url = new URL("https://habr.com/ru/rss/hub/java/");
        XmlReader.setDefaultEncoding("UTF-8");
        var feed = new SyndFeedInput().build(new XmlReader(url));
        System.out.println("Feed title: " + feed.getTitle());
        for (SyndEntry entry : feed.getEntries()) {
            var text = "Entry: " + entry.getUri()
                    + "\n - title: " + entry.getTitle()
                    + "\n - description: " + entry.getDescription().getValue();
            System.out.println(text);
        }
    }

}
