package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.PhotoRetrievalException;
import com.springbootproject.seasidehotel.response.RoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Validated
@RequestMapping(value = "/rooms")
public interface RoomApi {

    @Operation(summary = "Adds a Room", operationId = "addNewRoom", description = "This operation creates a Room entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = RoomResponse.class)))})
    @RequestMapping(value = "/add/new-room", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<RoomResponse> addNewRoom(@Parameter(description = "Photo of the Room") @RequestParam("photo") MultipartFile photo,
                                            @Parameter(description = "Type of the Room") @Valid @RequestParam("roomType") String roomType,
                                            @Parameter(description = "Price of the Room") @Valid @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException;

    @Operation(summary = "Gets Room types", operationId = "roomTypes", description = "This operation gets all types of Room entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))})
    @RequestMapping(value = "/room/types", method = RequestMethod.GET)
    List<String> roomTypes();

    @Operation(summary = "Gets Room objects", operationId = "getRooms", description = "This operation gets all Role entities.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class))))})
    @RequestMapping(value = "/all-rooms", method = RequestMethod.GET)
    ResponseEntity<List<RoomResponse>> getRooms() throws SQLException, PhotoRetrievalException;

    @Operation(summary = "Deletes a Room object", operationId = "deleteRoom", description = "This operation deletes a Room entity.")
    @RequestMapping(value = "/delete/room/{roomId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<Void> deleteRoom(@Parameter(description = "Identifier of the Room", required = true) @PathVariable Long roomId);

    @Operation(summary = "Updates a Room object", operationId = "updateRoom", description = "This operation updates a Room entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = RoomResponse.class)))})
    @RequestMapping(value = "/update/{roomId}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<RoomResponse> updateRoom(@Parameter(description = "Identifier of the Room", required = true) @PathVariable Long roomId,
                                            @Parameter(description = "Type of the Room") @RequestParam(required = false) String roomType,
                                            @Parameter(description = "Price of the Room") @RequestParam(required = false) BigDecimal roomPrice,
                                            @Parameter(description = "Photo of the Room") @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException;

    @Operation(summary = "Gets a Room object", operationId = "getRoomById", description = "This operation gets a Room entity by its identification.")
    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET)
    ResponseEntity<Optional<RoomResponse>> getRoomById(@Parameter(description = "Identifier of the Room", required = true) @PathVariable Long roomId);

    @Operation(summary = "Gets all available Room objects", operationId = "getAvailableRooms", description = "This operation gets all Room entities that are not booked.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoomResponse.class))))})
    @RequestMapping(value = "/available-rooms", method = RequestMethod.GET)
    ResponseEntity<List<RoomResponse>> getAvailableRooms(@Parameter(description = "Date of Check-In") @Valid @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                         @Parameter(description = "Date of Check-Out") @Valid @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                         @Parameter(description = "Type of the Room") @Valid @RequestParam("roomType") String roomType) throws SQLException;
}
