package ru.simbir.internship.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.internship.chat.domain.Message;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findAllByRoom_IdOrderByCreatedDesc(Pageable pageable, UUID chatRoomId);
}
