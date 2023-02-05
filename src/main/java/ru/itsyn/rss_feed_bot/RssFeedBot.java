package ru.itsyn.rss_feed_bot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class RssFeedBot extends TelegramLongPollingBot {

    final Config config;

    final Set<String> subscriptions = new LinkedHashSet<>();

    @Override
    public String getBotUsername() {
        return "rss_feed_pbot";
    }

    @Override
    public String getBotToken() {
        return config.token();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            processUpdate(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void processUpdate(Update update) throws Exception {
        var message = update.getMessage();
        if (message == null || !message.isCommand())
            return;
        var text = message.getText();
        if (isBlank(text))
            return;
        var chatId = message.getChatId();
        var arguments = text.split(" ");
        if (text.startsWith("/list")) {
            var replyText = new StringBuilder();
            replyText.append("Current subscriptions:");
            for (String url : subscriptions) {
                replyText.append("\n - ").append(url);
            }
            sendText(chatId, replyText.toString());
        } else if (text.startsWith("/add")) {
            var url = arguments[1];
            subscriptions.add(url);
            sendText(chatId, "Subscription is added: " + url);
        } else if (text.startsWith("/remove")) {
            var url = arguments[1];
            subscriptions.remove(url);
            sendText(chatId, "Subscription is removed: " + url);
        }
    }

    protected void sendText(Long chatId, String text) throws Exception {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        execute(message);
    }

    @ConfigurationProperties("rss-feed-bot")
    public record Config(
            String token
    ) {
    }

}
