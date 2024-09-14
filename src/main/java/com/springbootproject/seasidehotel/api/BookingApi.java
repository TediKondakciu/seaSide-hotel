package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.response.BookingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Api(value = "booking")
@RequestMapping(value = "/bookings")
public interface BookingApi {

    @ApiOperation(value = "Gets Booking objects", nickname = "getAllBookings", notes = "This operation gets all Booking entities", response = BookingResponse.class, responseContainer = "List")
    @RequestMapping(value = "all-bookings", method = RequestMethod.GET)
    ResponseEntity<List<BookingResponse>> getAllBookings();

    @ApiOperation(value = "Gets a Booking", nickname = "getBookingByConfirmationCode", notes = "This operation gets a Booking entity by its email.", response = BookingResponse.class)
    @RequestMapping(value = "/confirmation/{confirmationCode}", method = RequestMethod.GET)
    ResponseEntity<?> getBookingByConfirmationCode(@ApiParam(value = "Confirmation code of the Booking", required = true) @PathVariable String confirmationCode);

    @ApiOperation(value = "Saves a Booking", nickname = "saveBooking", notes = "This operation saves a Booking entity.", response = String.class)
    @RequestMapping(value = "/room/{roomId}/booking", method = RequestMethod.POST)
    ResponseEntity<?> saveBooking(@ApiParam(value = "Identifier of the Room", required = true) @PathVariable Long roomId, @ApiParam(value = "The Booking to be created", required = true) @Valid @RequestBody Booking bookingRequest);

    @ApiOperation(value = "Cancels a Booking", nickname = "cancelBooking", notes = "This operation cancels a Booking entity by its identification.")
    @RequestMapping(value = "/booking/{bookingId}/delete", method = RequestMethod.DELETE)
    void cancelBooking(@ApiParam(value = "Identifier of the Booking", required = true) @PathVariable Long bookingId);
}
