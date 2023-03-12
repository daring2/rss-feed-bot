package ru.itsyn.rss_feed_bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@RequiredArgsConstructor
public class BotSession {

    final RssFeedBot bot;
    final Long chatId;

    final Set<String> subscriptions = new LinkedHashSet<>();

    String lastCommand;

    public void processUpdate(Update update) throws Exception {
        var message = update.getMessage();
        if (message == null)
            return;
        var arguments = parseArguments(message.getText());
        if (arguments.size() == 0)
            return;
        var command = substringBefore(arguments.get(0), "@");
        if ("/list".equals(command)) {
            sendListText("Current subscriptions", subscriptions);
            lastCommand = null;
            return;
        }
        if (arguments.size() == 1) {
            sendText("Please specify feed URLs");
            return;
        }
        var feedUrls = arguments.subList(1, arguments.size());
        if ("/add".equals(command)) {
            feedUrls.forEach(subscriptions::add);
            sendListText("The subscriptions are added", feedUrls);
        } else if ("/remove".equals(command)) {
            feedUrls.forEach(subscriptions::remove);
            sendListText("The subscriptions are removed", feedUrls);
        }
        lastCommand = null;
    }

    protected List<String> parseArguments(String text) {
        if (isBlank(text))
            return emptyList();
        var arguments = new ArrayList<String>();
        var isCommand = text.startsWith("/");
        if (!isCommand && lastCommand != null) {
            arguments.add(lastCommand);
        }
        arguments.addAll(asList(text.split("[\\s]")));
        if (isCommand && arguments.size() == 1) {
            lastCommand = arguments.get(0);
        }
        return arguments;
    }

    protected void sendListText(String title, Collection<String> items) throws Exception {
        var text = new StringBuilder();
        text.append(title).append(":");
        for (String item : items) {
            text.append("\n - ").append(item);
        }
        sendText(text.toString());
    }

    protected void sendText(String text) throws Exception {
        var message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        bot.execute(message);
    }

}
