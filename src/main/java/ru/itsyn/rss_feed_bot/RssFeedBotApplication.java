package ru.itsyn.rss_feed_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.starter.TelegramBotStarterConfiguration;

@SpringBootApplication
@ConfigurationPropertiesScan
@Import(TelegramBotStarterConfiguration.class)
public class RssFeedBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssFeedBotApplication.class, args);
    }

}
