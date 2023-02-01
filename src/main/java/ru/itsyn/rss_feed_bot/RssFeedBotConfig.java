package ru.itsyn.rss_feed_bot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rss-feed-bot")
public record RssFeedBotConfig(
        String token
) {
}
