package ru.simbir.internship.chat.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@RedisHash("BlockedUser")
@NoArgsConstructor
@AllArgsConstructor
public class BlockedUser implements Serializable {
    private UUID id;
}