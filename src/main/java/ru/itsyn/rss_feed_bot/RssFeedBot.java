package ru.itsyn.rss_feed_bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RssFeedBot extends TelegramLongPollingBot {

    final Config config;

    public RssFeedBot(Config config) {
        super(config.token);
        this.config = config;
    }

    final Map<Long, BotSession> sessions = new ConcurrentHashMap<>();

    @Override
    public String getBotUsername() {
        return "rss_feed_pbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            var message = update.getMessage();
            if (message == null)
                return;
            var session = getSession(message.getChatId());
            session.processUpdate(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected BotSession getSession(Long chatId) {
        return sessions.computeIfAbsent(
                chatId,
                (id) -> new BotSession(this, id)
        );
    }


    @ConfigurationProperties("rss-feed-bot")
    public record Config(
            String token
    ) {
    }

}
