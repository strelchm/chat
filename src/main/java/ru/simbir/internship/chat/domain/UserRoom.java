package ru.simbir.internship.chat.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Чат-комната <===> пользователь чата
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user", "room"})
public class UserRoom {
    @Embeddable
    public static class Key implements Serializable {
        @Column(name = "user_id")
        private UUID userId;
        @Column(name = "room_id")
        private UUID roomId;

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public UUID getRoomId() {
            return roomId;
        }

        public void setRoomId(UUID roomId) {
            this.roomId = roomId;
        }
    }

    @EmbeddedId
    private UserRoom.Key id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRoomRole userRoomRole;

    private LocalDateTime blockedTime;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date updated;
}
