package ru.simbir.internship.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.internship.chat.domain.Room;
import ru.simbir.internship.chat.domain.UserRoom;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
   // Optional<Room> findByUserRooms_User(UUID userId, UUID roomId);
}
