package ru.itsyn.rss_feed_bot.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscription", uniqueConstraints = {
        @UniqueConstraint(name = "uc_subscription_chat_id", columnNames = {"chat_id", "feed_url"})
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "feed_url", nullable = false)
    private String feedUrl;

}