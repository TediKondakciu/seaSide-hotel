package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.exception.InvalidBookingRequestException;
import com.springbootproject.seasidehotel.exception.ResourceNotFoundException;
import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.model.Room;
import com.springbootproject.seasidehotel.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomService roomService;

    @BeforeEach
    void setUp(){
        bookingService = new BookingService(bookingRepository, roomService);
    }

    @Test
    void canGetAllBookingsByRoomId() {
        Long roomId = 1L;
        // Create two Bookings
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, null, null);
        Booking booking2 = new Booking(2L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), "Idet Kondakci", "idet@example.com", 1, 0, 1, null, null);

        // Arrange
        when(bookingRepository.findByRoomId(roomId)).thenReturn(Arrays.asList(booking1, booking2));

        // Call the method to get the Bookings for a Room
        List<Booking> expectedBookings = bookingService.getAllBookingsByRoomId(roomId);

        // Test if the expected Bookings are retrieved
        assertNotNull(expectedBookings);
        assertEquals(2, expectedBookings.size());
        assertEquals("Tedi Kondakciu", expectedBookings.get(0).getGuestFullName());
        assertEquals("Idet Kondakci", expectedBookings.get(1).getGuestFullName());

        // Verify that one call towards the repository has been performed
        verify(bookingRepository, times(1)).findByRoomId(roomId);
    }

    @Test
    void canGetAllBookingsByRoomId_NoBooking(){
        Long roomId = 1L;

        // Arrange
        when(bookingRepository.findByRoomId(roomId)).thenReturn(List.of());

        // Call the method to get the Bookings for a Room
        List<Booking> expectedBookings = bookingService.getAllBookingsByRoomId(roomId);

        // Test that an empty list of Bookings is retrieved
        assertNotNull(expectedBookings);
        assertTrue(expectedBookings.isEmpty());

        // Verify that one call towards the repository has been performed
        verify(bookingRepository, times(1)).findByRoomId(roomId);
    }

    @Test
    void canGetAllBookings() {
        // Create two Bookings
        Booking booking1 = new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, null, null);
        Booking booking2 = new Booking(2L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), "Idet Kondakci", "idet@example.com", 1, 0, 1, null, null);

        // Arrange
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        // Call the method to retrieve all Bookings
        List<Booking> expectedBookings = bookingService.getAllBookings();

        // Test if all Bookings are retrieved correctly
        assertNotNull(expectedBookings);
        assertEquals(2, expectedBookings.size());
        assertEquals("Tedi Kondakciu", expectedBookings.get(0).getGuestFullName());
        assertEquals("Idet Kondakci", expectedBookings.get(1).getGuestFullName());

        // Verify that one call towards the repository has been performed
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void canGetAllBookings_EmptyList() {
        // Arrange
        when(bookingRepository.findAll()).thenReturn(List.of());

        // Call the method to retrieve all Bookings
        List<Booking> expectedBookings = bookingService.getAllBookings();

        // Test if empty list of Bookings is retrieved
        assertNotNull(expectedBookings);
        assertTrue(expectedBookings.isEmpty());

        // Verify that one call towards the repository has been performed
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void canFindByBookingConfirmationCode() {
        String confirmationCode = "12345";

        // Create a Booking
        Booking booking = new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, confirmationCode, null);

        // Arrange
        when(bookingRepository.findByBookingConfirmationCode(confirmationCode)).thenReturn(Optional.of(booking));

        // Call the method to retrieve Booking
        Booking expectedBooking = bookingService.findByBookingConfirmationCode(confirmationCode);

        // Test if Booking is retrieved successfully
        assertNotNull(expectedBooking);
        assertEquals(confirmationCode, expectedBooking.getBookingConfirmationCode());

        // Verify interaction with repository
        verify(bookingRepository, times(1)).findByBookingConfirmationCode(confirmationCode);
    }

    @Test
    void cannotFindByBookingConfirmationCode_NoBooking() {
        String confirmationCode = "12345";

        // Arrange
        when(bookingRepository.findByBookingConfirmationCode(confirmationCode)).thenReturn(Optional.empty());

        // Call the method to retrieve Booking
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.findByBookingConfirmationCode(confirmationCode);
        });

        // Test that Confirmation Code does not match any Booking
        assertEquals("No booking found with booking code:" + confirmationCode, exception.getMessage());

        // Verify interaction with repository
        verify(bookingRepository, times(1)).findByBookingConfirmationCode(confirmationCode);
    }

    @Test
    void canSaveBooking_Success() {
        Long roomId = 1L;
        // Create two Booking
        Booking booking = new Booking(null, LocalDate.now(), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, null, null);

        // Create a Room
        Room room = new Room();
        room.setBookings(new ArrayList<>());

        // Arrange
        when(roomService.getRoomById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Call the method to save the Booking
        String confirmationCode = bookingService.saveBooking(roomId, booking);

        // Test that the retrieved Confirmation Code is correct
        assertNotNull(confirmationCode);
        assertEquals(booking.getBookingConfirmationCode(), confirmationCode);
        verify(bookingRepository, times(1)).save(booking);
        verify(roomService, times(1)).getRoomById(roomId);
    }

    @Test
    void cannotSaveBooking_InvalidCheckInOutDate(){
        Long roomId = 1L;
        // Create two Booking
        Booking booking = new Booking(null, LocalDate.now().plusDays(3), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, null, null);

        InvalidBookingRequestException exception = assertThrows(InvalidBookingRequestException.class, () -> {
           bookingService.saveBooking(roomId, booking);
        });

        assertEquals("Check-in date should come before check-out date!", exception.getMessage());
    }

    @Test
    void cannotSaveBooking_RoomNotAvailable(){
        Long roomId = 1L;
        // Create two Booking
        Booking booking = new Booking(null, LocalDate.now(), LocalDate.now().plusDays(2), "Tedi Kondakciu", "tedi@example.com", 2, 1, 3, null, null);

        // Create an existing booking that overlaps with the bookingToSave
        Booking existingBooking = new Booking(null, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), "Existing Guest", "existing@example.com", 2, 1, 3, null, null);

        // Create a Room
        Room room = new Room();
        room.setBookings(new ArrayList<>(Collections.singletonList(existingBooking)));

        // Arrange
        when(roomService.getRoomById(roomId)).thenReturn(Optional.of(room));

        InvalidBookingRequestException exception = assertThrows(InvalidBookingRequestException.class, () -> {
           bookingService.saveBooking(roomId, booking);
        });

        assertEquals("This room is not available for the selected period of time!", exception.getMessage());
    }

    @Test
    void cancelBooking() {
        Long bookingId = 1L;
        bookingService.cancelBooking(bookingId);
        verify(bookingRepository).deleteById(bookingId);
    }
}