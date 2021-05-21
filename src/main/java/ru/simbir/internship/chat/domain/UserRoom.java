package ru.simbir.internship.chat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "room_user")
public class UserRoom {

    @Embeddable
    private static class Key implements Serializable {
        @Column(name = "user_id")
        private Long userId;
        @Column(name = "room_id")
        private Long roomId;
    }

    @EmbeddedId
    private Key id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @Column(nullable = false)
    private Boolean owner;

    private Boolean moderator;

    private Boolean blocked;

    @Column(name = "block_until")
    private Date blockUntil;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

}
