package org.telegram.bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.bot.domain.entities.Chat;
import org.telegram.bot.domain.entities.NewsSource;

import java.util.List;

/**
 * Spring Data repository for the News entity.
 */
public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
    NewsSource findByChatAndId(Chat chat, Long newsSourceId);
    NewsSource findByChatAndName(Chat chat, String name);
    List<NewsSource> findByChat(Chat chat);
}
