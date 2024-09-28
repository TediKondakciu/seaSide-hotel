package com.springbootproject.seasidehotel.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Tedi Kondakçiu
 */

@Schema(description = "Booking is an abstract entity that describes the common set of properties in order to book a room.")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(name = "check_In")
    @NotNull(message = "CheckInDate cannot be null!")
    private LocalDate checkInDate;

    @Column(name = "check_Out")
    @NotNull(message = "CheckOutDate cannot be null!")
    private LocalDate checkOutDate;

    @Column(name = "guest_FullName")
    @NotNull(message = "FullName cannot be null!")
    private String guestFullName;

    @Column(name = "guest_Email")
    @NotNull(message = "Email cannot be null!")
    private String guestEmail;

    @Column(name = "adults")
    @NotNull(message = "Number of Adults cannot be null!")
    private int numOfAdults;

    @Column(name = "children")
    @NotNull
    private int numOfChildren;

    @Column(name = "total_guests")
    private int totalNumberOfGuests;

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalGuests(){
        this.totalNumberOfGuests = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalGuests();
    }
}
