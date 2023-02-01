package ru.itsyn.rss_feed_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RssFeedBot extends TelegramLongPollingBot {

    @Autowired
    protected RssFeedBotConfig config;

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
        execute(SendMessage.builder()
                .chatId(message.getFrom().getId())
                .text("response: " + message.getText())
                .build()
        );
    }

}
