package ru.itsyn.rss_feed_bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.itsyn.rss_feed_bot.subscription.Subscription;
import ru.itsyn.rss_feed_bot.subscription.SubscriptionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@RequiredArgsConstructor
public class BotSession {

    final RssFeedBot bot;
    final Long chatId;

    String lastCommand;

    public void processUpdate(Update update) throws Exception {
        var message = update.getMessage();
        if (message == null)
            return;
        var arguments = parseArguments(message.getText());
        if (arguments.size() == 0)
            return;
        var command = arguments.get(0);
        if ("/list".equals(command)) {
            var feedUrls = findCurrentSubscriptions();
            sendListText("Current subscriptions", feedUrls);
            lastCommand = null;
            return;
        }
        if (arguments.size() == 1) {
            sendText("Please specify feed URLs");
            return;
        }
        var feedUrls = parseFeedUrls(arguments);
        if ("/add".equals(command)) {
            feedUrls.forEach(this::addSubscription);
            sendListText("The subscriptions are added", feedUrls);
        } else if ("/remove".equals(command)) {
            feedUrls.forEach(this::removeSubscription);
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
        var command = arguments.get(0);
        if (isCommand && command.contains("@")) {
            command = substringBefore(command, "@");
            arguments.set(0, command);
        }
        if (isCommand && arguments.size() == 1) {
            lastCommand = command;
        }
        return arguments;
    }

    protected List<String> parseFeedUrls(List<String> arguments) {
        //TODO normalize URLs
        return arguments.subList(1, arguments.size());
    }

    protected List<String> findCurrentSubscriptions() {
        return subscriptionRepository().findFeedUrlsByChatId(chatId);
    }

    protected void addSubscription(String feedUrl) {
        var subscription = subscriptionRepository()
                .findByChatIdAndFeedUrl(chatId, feedUrl);
        if (subscription != null)
            return;
        subscription = new Subscription();
        subscription.setChatId(chatId);
        subscription.setFeedUrl(feedUrl);
        subscriptionRepository().save(subscription);
    }

    protected void removeSubscription(String feedUrl) {
        var subscription = subscriptionRepository()
                .findByChatIdAndFeedUrl(chatId, feedUrl);
        if (subscription == null)
            return;
        subscriptionRepository().delete(subscription);
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

    final SubscriptionRepository subscriptionRepository() {
        return bot.context.subscriptionRepository;
    }

}
