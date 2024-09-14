package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.PhotoRetrievalException;
import com.springbootproject.seasidehotel.response.RoomResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
@Api(value = "room")
@RequestMapping(value = "/rooms")
public interface RoomApi {

    @ApiOperation(value = "Adds a Room", nickname = "addNewRoom", notes = "This operation creates a Room entity.", response = RoomResponse.class)
    @RequestMapping(value = "/add/new-room", method = RequestMethod.POST)
    ResponseEntity<RoomResponse> addNewRoom(@ApiParam(value = "Photo of the Room") @RequestParam("photo") MultipartFile photo,
                                            @ApiParam(value = "Type of the Room") @Valid @RequestParam("roomType") String roomType,
                                            @ApiParam(value = "Price of the Room") @Valid @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException;

    @ApiOperation(value = "Gets Room types", nickname = "roomTypes", notes = "This operation gets all types of Room entity.", response = String.class, responseContainer = "List")
    @RequestMapping(value = "/room/types", method = RequestMethod.GET)
    List<String> roomTypes();

    @ApiOperation(value = "Gets Room objects", nickname = "getRooms", notes = "This operation gets all Role entities.", response = RoomResponse.class, responseContainer = "List")
    @RequestMapping(value = "/all-rooms", method = RequestMethod.GET)
    ResponseEntity<List<RoomResponse>> getRooms() throws SQLException, PhotoRetrievalException;

    @ApiOperation(value = "Deletes a Room object", nickname = "deleteRoom", notes = "This operation deletes a Room entity.")
    @RequestMapping(value = "/delete/room/{roomId}", method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteRoom(@ApiParam(value = "Identifier of the Room", required = true) @PathVariable Long roomId);

    @ApiOperation(value = "Updates a Room object", nickname = "updateRoom", notes = "This operation updates a Room entity.", response = RoomResponse.class)
    @RequestMapping(value = "/update/{roomId}", method = RequestMethod.PUT)
    ResponseEntity<RoomResponse> updateRoom(@ApiParam(value = "Identifier of the Room", required = true) @PathVariable Long roomId,
                                            @ApiParam(value = "Type of the Room") @RequestParam(required = false) String roomType,
                                            @ApiParam(value = "Price of the Room") @RequestParam(required = false) BigDecimal roomPrice,
                                            @ApiParam(value = "Photo of the Room") @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException;

    @ApiOperation(value = "Gets a Room object", nickname = "getRoomById", notes = "This operation gets a Room entity by its identification.")
    @RequestMapping(value = "/room/{roomId}", method = RequestMethod.GET)
    ResponseEntity<Optional<RoomResponse>> getRoomById(@ApiParam(value = "Identifier of the Room", required = true) @PathVariable Long roomId);

    @ApiOperation(value = "Gets all available Room objects", nickname = "getAvailableRooms", notes = "This operation gets all Room entities that are not booked.", response = RoomResponse.class, responseContainer = "List")
    @RequestMapping(value = "/available-rooms", method = RequestMethod.GET)
    ResponseEntity<List<RoomResponse>> getAvailableRooms(@ApiParam(value = "Date of Check-In") @Valid @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                         @ApiParam(value = "Date of Check-Out") @Valid @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                         @ApiParam(value = "Type of the Room") @Valid @RequestParam("roomType") String roomType) throws SQLException;
}
