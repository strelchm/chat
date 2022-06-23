package ru.simbir.internship.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.simbir.internship.chat.domain.BlockedUser;

import java.util.UUID;

@Repository
public interface BlockedUserRepository extends CrudRepository<BlockedUser, UUID> {
}