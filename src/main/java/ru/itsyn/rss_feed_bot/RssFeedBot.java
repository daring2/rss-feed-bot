package ru.itsyn.rss_feed_bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class RssFeedBot extends TelegramLongPollingBot {

    final Config config;

    public RssFeedBot(Config config) {
        super(config.token);
        this.config = config;
    }

    final Set<String> subscriptions = new LinkedHashSet<>();

    @Override
    public String getBotUsername() {
        return "rss_feed_pbot";
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
        var chatId = message.getChatId();
        var arguments = parseArguments(message.getText());
        if (arguments.size() == 0)
            return;
        var command = arguments.get(0);
        if ("/list".equals(command)) {
            var replyText = new StringBuilder();
            replyText.append("Current subscriptions:");
            for (String url : subscriptions) {
                replyText.append("\n - ").append(url);
            }
            sendText(chatId, replyText.toString());
        } else if ("/add".equals(command)) {
            var url = arguments.get(1);
            subscriptions.add(url);
            sendText(chatId, "The subscription is added: " + url);
        } else if ("/remove".equals(command)) {
            var url = arguments.get(1);
            subscriptions.remove(url);
            sendText(chatId, "The subscription is removed: " + url);
        }
    }

    protected List<String> parseArguments(String text) {
        if (isBlank(text))
            return emptyList();
        return asList(text.split(" "));
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
