package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.response.BookingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping(value = "/bookings")
public interface BookingApi {

    @Operation(summary = "Gets Booking objects", operationId = "getAllBookings", description = "This operation gets all Booking entities")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class))))})
    @RequestMapping(value = "all-bookings", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<List<BookingResponse>> getAllBookings();

    @Operation(summary = "Gets a Booking", operationId = "getBookingByConfirmationCode", description = "This operation gets a Booking entity by its email.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = BookingResponse.class)))})
    @RequestMapping(value = "/confirmation/{confirmationCode}", method = RequestMethod.GET)
    ResponseEntity<?> getBookingByConfirmationCode(@Parameter(description = "Confirmation code of the Booking", required = true) @PathVariable String confirmationCode);

    @Operation(summary = "Saves a Booking", operationId = "saveBooking", description = "This operation saves a Booking entity.")
    @RequestMapping(value = "/room/{roomId}/booking", method = RequestMethod.POST)
    ResponseEntity<?> saveBooking(@Parameter(description = "Identifier of the Room", required = true) @PathVariable Long roomId, @Parameter(description = "The Booking to be created", required = true) @Valid @RequestBody Booking bookingRequest);

    @Operation(summary = "Cancels a Booking", operationId = "cancelBooking", description = "This operation cancels a Booking entity by its identification.")
    @RequestMapping(value = "/booking/{bookingId}/delete", method = RequestMethod.DELETE)
    void cancelBooking(@Parameter(description = "Identifier of the Booking", required = true) @PathVariable Long bookingId);
}
