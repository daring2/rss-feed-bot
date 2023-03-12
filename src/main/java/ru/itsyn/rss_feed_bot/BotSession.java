package ru.itsyn.rss_feed_bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@RequiredArgsConstructor
public class BotSession {

    final RssFeedBot bot;
    final Long chatId;

    final Set<String> subscriptions = new LinkedHashSet<>();

    public void processUpdate(Update update) throws Exception {
        var message = update.getMessage();
        if (message == null || !message.isCommand())
            return;
        var arguments = parseArguments(message.getText());
        if (arguments.size() == 0)
            return;
        var command = substringBefore(arguments.get(0), "@");
        if ("/list".equals(command)) {
            var replyText = new StringBuilder();
            replyText.append("Current subscriptions:");
            for (String url : subscriptions) {
                replyText.append("\n - ").append(url);
            }
            sendText(replyText.toString());
        } else if ("/add".equals(command)) {
            var url = arguments.get(1);
            subscriptions.add(url);
            sendText("The subscription is added: " + url);
        } else if ("/remove".equals(command)) {
            var url = arguments.get(1);
            subscriptions.remove(url);
            sendText("The subscription is removed: " + url);
        }
    }

    protected List<String> parseArguments(String text) {
        if (isBlank(text))
            return emptyList();
        return asList(text.split("\\s"));
    }

    protected void sendText(String text) throws Exception {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        bot.execute(message);
    }

}
