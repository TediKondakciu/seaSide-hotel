package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    private Booking booking;

    @BeforeEach
    void setUp(){
        // Create a booking
        booking = new Booking();
        booking.setGuestFullName("John");
        booking.setGuestEmail("john@outlook.com");
        booking.setCheckInDate(LocalDate.of(2024, 9, 20));
        booking.setCheckOutDate(LocalDate.of(2024, 9, 25));
    }

    @AfterEach
    void tearDown() {bookingRepository.deleteAll();}

    @Test
    public void testFindByRoomId() {
        Room room = new Room();
        room.setRoomType("Single");
        room.setRoomPrice(BigDecimal.valueOf(200.00));
        room.setBooked(false);
        roomRepository.save(room);

        room.addBookings(booking);
        bookingRepository.save(booking);

        List<Booking> actualBookings = bookingRepository.findByRoomId(room.getId());

        assertThat(actualBookings).hasSize(1);
        assertThat(actualBookings.get(0).getGuestFullName()).isEqualTo("John");
        assertThat(actualBookings.get(0).getRoom()).isEqualTo(room);
    }

    @Test
    public void testFindByRoomId_NoBookings() {
        Room room = new Room();
        room.setRoomType("Double");
        room.setRoomPrice(BigDecimal.valueOf(150.00));
        room.setBooked(false);
        roomRepository.save(room);

        List<Booking> actualBookings = bookingRepository.findByRoomId(room.getId());

        assertThat(actualBookings).isEmpty();
    }

    @Test
    void itShouldFindBookingConfirmationCode(){
        // Save the booking
        bookingRepository.save(booking);

        String confirmationCode = booking.getBookingConfirmationCode();

        Booking expected = bookingRepository.findByBookingConfirmationCode(confirmationCode).get();

        assertThat(booking).isEqualTo(expected);
    }
}
