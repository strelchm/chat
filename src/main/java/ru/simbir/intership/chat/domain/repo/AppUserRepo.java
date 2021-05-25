package ru.simbir.intership.chat.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.intership.chat.domain.dbo.AppUser;
import ru.simbir.intership.chat.domain.dbo.AppUserRole;
import ru.simbir.intership.chat.domain.dbo.ChatRoomMembership;
import ru.simbir.intership.chat.domain.dbo.UserStatus;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Репозиторий для модели пользователь приложения
 */
@Repository
public interface AppUserRepo extends JpaRepository<AppUser, UUID> {
    Set<AppUser> findAllByStatus(UserStatus status);

    Set<AppUser> findAllByRole(AppUserRole role);

    Set<AppUser> findAllByChatRoomMembershipsIn(Set<ChatRoomMembership> chatRoomMemberships);

    Optional<AppUser> findByLogin(String login);
}