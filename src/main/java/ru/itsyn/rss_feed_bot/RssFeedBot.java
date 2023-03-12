package ru.itsyn.rss_feed_bot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.itsyn.rss_feed_bot.subscription.SubscriptionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RssFeedBot extends TelegramLongPollingBot {

    final Context context;
    final Config config;

    public RssFeedBot(Context context, Config config) {
        super(config.token);
        this.context = context;
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
        return sessions.computeIfAbsent(chatId,
                (id) -> new BotSession(this, id)
        );
    }

    @Component
    @RequiredArgsConstructor
    public static class Context {
        final SubscriptionRepository subscriptionRepository;
    }

    @ConfigurationProperties("rss-feed-bot")
    public record Config(
            String token
    ) {}

}
