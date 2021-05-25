package ru.simbir.intership.chat.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.intership.chat.domain.dbo.AppUser;
import ru.simbir.intership.chat.domain.dbo.ChatRoom;
import ru.simbir.intership.chat.domain.dbo.ChatRoomMembership;
import ru.simbir.intership.chat.domain.dbo.ChatRoomRole;

import java.util.Set;
import java.util.UUID;

/**
 * Репозиторий для модели чат-комнаты
 */
@Repository
public interface ChatRoomMembershipRepo extends JpaRepository<ChatRoomMembership, UUID> {
    Set<ChatRoomMembership> findAllByChatRoom(ChatRoom chatRoom);

    Set<ChatRoomMembership> findAllByAppUser(AppUser appUser);

    Set<ChatRoomMembership> findAllByAppUserAndRole(AppUser appUser, ChatRoomRole role);

    Set<ChatRoomMembership> findAllByChatRoomAndAppUser(ChatRoom chatRoom, AppUser appUser);
}
