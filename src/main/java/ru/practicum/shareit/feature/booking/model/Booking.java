package ru.practicum.shareit.feature.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.feature.item.model.Item;
import ru.practicum.shareit.feature.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings")
public class Booking {
    public enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    User booker;

    LocalDateTime startDate;

    LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    Status status;

    Long requestId;
}
