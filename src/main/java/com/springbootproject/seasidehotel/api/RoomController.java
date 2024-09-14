package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.PhotoRetrievalException;
import com.springbootproject.seasidehotel.exception.ResourceNotFoundException;
import com.springbootproject.seasidehotel.model.Booking;
import com.springbootproject.seasidehotel.model.Room;
import com.springbootproject.seasidehotel.response.BookingResponse;
import com.springbootproject.seasidehotel.response.RoomResponse;
import com.springbootproject.seasidehotel.service.IBookingService;
import com.springbootproject.seasidehotel.service.IRoomService;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Tedi Kondakçiu
 */

@RestController
public class RoomController implements RoomApi{
    private final IRoomService roomService;
    private final IBookingService bookingService;

    @Autowired
    public RoomController(IRoomService roomService, IBookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<RoomResponse> addNewRoom(@ApiParam(value = "Photo of the Room") @RequestParam("photo") MultipartFile photo,
                                                   @ApiParam(value = "Type of the Room") @Valid @RequestParam("roomType") String roomType,
                                                   @ApiParam(value = "Price of the Room") @Valid @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public List<String> roomTypes(){
        return roomService.getAllRoomTypes();
    }

    @Override
    public ResponseEntity<List<RoomResponse>> getRooms() throws SQLException, PhotoRetrievalException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }
        return new ResponseEntity<>(roomResponses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);

        return new ResponseEntity<>(roomResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {RoomResponse roomResponse = getRoomResponse(room);
                                    return new ResponseEntity<>(Optional.of(roomResponse), HttpStatus.OK);})
                .orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
    }

    @Override
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                                @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                                @RequestParam("roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : availableRooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length>0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if(roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else {
            return new ResponseEntity<>(roomResponses, HttpStatus.OK);
        }
    }

    private RoomResponse getRoomResponse(Room room) throws PhotoRetrievalException {
        List<Booking> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings.stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                                                   booking.getCheckInDate(),
                                                   booking.getCheckOutDate(),
                                                   booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            }
            catch(SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes, bookingInfo);
    }

    private List<Booking> getAllBookingsByRoomId(Long roomId){
        return bookingService.getAllBookingsByRoomId(roomId);
    }
}
