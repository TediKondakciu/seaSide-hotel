package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.InvalidBookingRequestException;
import com.springbootproject.seasidehotel.exception.ResourceNotFoundException;
import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.model.Room;
import com.springbootproject.seasidehotel.response.BookingResponse;
import com.springbootproject.seasidehotel.response.RoomResponse;
import com.springbootproject.seasidehotel.service.IBookingService;
import com.springbootproject.seasidehotel.service.IRoomService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;

    @Autowired
    public BookingController(IBookingService bookingService, IRoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping("all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(Booking booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return new ResponseEntity<>(bookingResponses, HttpStatus.OK);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@Parameter(description = "Confirmation code of the Booking", required = true) @PathVariable String confirmationCode){
        try{
            Booking booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
        }catch(ResourceNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@Parameter(description = "Identifier of the Room", required = true) @PathVariable Long roomId, @Parameter(description = "The Booking to be created", required = true) @Valid @RequestBody Booking bookingRequest){
        try{
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return new ResponseEntity<>("Room booked successfully! Your booking confirmation code is: " + confirmationCode, HttpStatus.OK);
        }catch(InvalidBookingRequestException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@Parameter(description = "Identifier of the Booking", required = true) @PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(Booking booking){
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice());

        return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(),
                                   booking.getCheckOutDate(), booking.getGuestFullName(),
                                   booking.getGuestEmail(), booking.getNumOfAdults(),
                                   booking.getNumOfChildren(), booking.getTotalNumberOfGuests(),
                                   booking.getBookingConfirmationCode(), room);
    }
}
