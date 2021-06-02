package ru.simbir.internship.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, UUID> {
    Optional<UserRoom> findByUser_IdAndRoom_Id(UUID userId, UUID roomId);

    List<UserRoom> findByUser_Id(UUID userId);
}