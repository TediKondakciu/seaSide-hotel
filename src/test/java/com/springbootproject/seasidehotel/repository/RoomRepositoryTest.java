package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @BeforeEach
    void setUp(){
        Room room1 = new Room(null, "Single", BigDecimal.valueOf(100), false, null, null);
        Room room2 = new Room(null, "Double", BigDecimal.valueOf(150), false, null, null);
        Room room3 = new Room(null, "Suite", BigDecimal.valueOf(200), false, null, null);
        Room room4 = new Room(null, "Single", BigDecimal.valueOf(120), false, null, null);

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
        roomRepository.save(room4);
    }

    @AfterEach
    void tearDown(){
        roomRepository.deleteAll();
    }

    @Test
    void itShouldFindDistinctRoomTypes(){
        List<String> expected = List.of("Single", "Double", "Suite");
        List<String> result = roomRepository.findDistinctRoomTypes();

        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testFindAvailableRoomsByDatesAndType() {
        // Setup a booking
        Booking booking = new Booking();
        booking.setGuestFullName("John");
        booking.setGuestEmail("john@outlook.com");
        booking.setCheckInDate(LocalDate.of(2024, 9, 20));
        booking.setCheckOutDate(LocalDate.of(2024, 9, 25));

        // Creating a room and adding the booking
        Room bookedRoom = new Room(null, "Double", BigDecimal.valueOf(150), false, null, null);
        bookedRoom.addBookings(booking);
        roomRepository.save(bookedRoom);

        // Testing the availability
        LocalDate checkInDate = LocalDate.of(2024, 9, 26);
        LocalDate checkOutDate = LocalDate.of(2024, 9, 30);
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, "Single");

        assertThat(availableRooms).hasSize(2); // Should return 2 available Single rooms
        assertThat(availableRooms).extracting("roomType").contains("Single");
    }
}
