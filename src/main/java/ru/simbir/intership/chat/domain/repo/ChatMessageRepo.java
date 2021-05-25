package ru.simbir.intership.chat.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.simbir.intership.chat.domain.dbo.AppUser;
import ru.simbir.intership.chat.domain.dbo.ChatMessage;
import ru.simbir.intership.chat.domain.dbo.ChatRoom;

import java.util.Set;
import java.util.UUID;

/**
 * Репозиторий для модели сообщения чата
 */
@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, UUID>, JpaSpecificationExecutor<AppUser> {
    Set<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);

    Set<ChatMessage> findAllByUser(AppUser user);

    Set<ChatMessage> findAllByChatRoomAndUser(ChatRoom chatRoom, AppUser user);
}
