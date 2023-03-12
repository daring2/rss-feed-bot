package ru.itsyn.rss_feed_bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class RssFeedBot extends AbilityBot {

    final Config config;

    public RssFeedBot(Config config) {
        super(config.token, "rss_feed_pbot");
        this.config = config;
    }

    final Set<String> subscriptions = new LinkedHashSet<>();

    @Override
    public long creatorId() {
        return 358790700;
    }

    public Ability createListCommand() {
        return Ability.builder()
                .name("list")
                .info("List current subscriptions")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    var replyText = new StringBuilder();
                    replyText.append("Current subscriptions:");
                    for (String url : subscriptions) {
                        replyText.append("\n - ").append(url);
                    }
                    silent.send(replyText.toString(), ctx.chatId());
                })
                .build();
    }

    public Ability createAddCommand() {
        return Ability.builder()
                .name("add")
                .info("Add a subscription")
                .input(1)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    var url = ctx.firstArg();
                    subscriptions.add(url);
                    silent.send("The subscription is added: " + url, ctx.chatId());
                })
                .build();
    }

    public Ability createRemoveCommand() {
        return Ability.builder()
                .name("remove")
                .info("Remove a subscription")
                .input(1)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    var url = ctx.firstArg();
                    subscriptions.remove(url);
                    silent.send("The subscription is removed: " + url, ctx.chatId());
                })
                .build();
    }

    @ConfigurationProperties("rss-feed-bot")
    public record Config(
            String token
    ) {
    }

}
