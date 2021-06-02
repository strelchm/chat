package ru.simbir.internship.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
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
public class UserRoom {
    @Embeddable
    private static class Key implements Serializable {
        @Column(name = "user_id")
        private UUID userId;
        @Column(name = "room_id")
        private UUID roomId;
    }

    @EmbeddedId
    private UserRoom.Key id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

//    @ElementCollection
//    private Set<UserRoomRole> userRoomRoles; // роли в чате подразумевают отдельные разграниченные права, разделять права при пересечении ролей геморно
    private UserRoomRole userRoomRole;

    private LocalDateTime blockedTime;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;
}
