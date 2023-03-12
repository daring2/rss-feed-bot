package ru.itsyn.rss_feed_bot.subscription;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    List<Subscription> findByChatId(Long chatId);

    Subscription findByChatIdAndFeedUrl(Long chatId, String feedUrl);

    @Query("select e.feedUrl from Subscription e where e.chatId = ?1 order by e.feedUrl")
    List<String> findFeedUrlsByChatId(Long chatId);

}