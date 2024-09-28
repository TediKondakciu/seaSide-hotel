package com.springbootproject.seasidehotel.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@Schema(description = "Room is an abstract entity that describes the common set of properties that a concrete room has.")
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String roomType;
    @NotNull
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;


    public Room(){
        this.bookings = new ArrayList<>(); // to avoid NullPointerException when a new room is added to database
    }

    public void addBookings(Booking booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        booking.setBookingConfirmationCode(RandomStringUtils.randomNumeric(10));
    }
}
