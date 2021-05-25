package ru.simbir.intership.chat.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.intership.chat.domain.dbo.ChartRoomType;
import ru.simbir.intership.chat.domain.dbo.ChatRoom;
import ru.simbir.intership.chat.domain.dbo.ChatRoomMembership;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Репозиторий для модели чат-комнаты
 */
@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom, UUID> {
    Optional<ChatRoom> findByName(String name);

    Optional<ChatRoom> findByType(ChartRoomType type);

    Optional<ChatRoom> findByChatRoomMembershipsIn(Set<ChatRoomMembership> chatRoomMembershipSet);
}
