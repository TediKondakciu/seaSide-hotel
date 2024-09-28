package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.exception.ResourceNotFoundException;
import com.springbootproject.seasidehotel.model.Room;
import com.springbootproject.seasidehotel.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    private RoomService roomService;
    private Room room;
    private MockMultipartFile file = new MockMultipartFile("photo", "room.jpg", "image/jpeg", "test image".getBytes());;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        roomService = new RoomService(roomRepository);
    }

    @AfterEach
    void tearDown(){
        roomRepository.deleteAll();
    }

    @Test
    void canAddNewRoom_withFile() throws SQLException, IOException {
        // Create a Room
        room = new Room(null, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Use method to add a Room
        Room expectedRoom = roomService.addNewRoom(file, room.getRoomType(), room.getRoomPrice());

        verify(roomRepository).save(any(Room.class));
        assertEquals(room.getRoomType(), expectedRoom.getRoomType());
        assertEquals(room.getRoomPrice(), expectedRoom.getRoomPrice());
        assertNotNull(room.getPhoto());
    }

    @Test
    public void cannotAddNewRoom_WithoutFile() throws SQLException, IOException {
        // Create a Room with empty photo
        room = new Room(null, "Single", BigDecimal.valueOf(100), false, null, null);

        // Create a MockMultipartFile that is empty
        MockMultipartFile emptyFile = new MockMultipartFile("photo", "", "image/jpeg", new byte[0]);

        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Use method to add a Room
        Room expectedRoom = roomService.addNewRoom(emptyFile, room.getRoomType(), room.getRoomPrice());

        // Assert
        verify(roomRepository).save(any(Room.class));
        assertEquals(room.getRoomType(), expectedRoom.getRoomType());
        assertEquals(room.getRoomPrice(), expectedRoom.getRoomPrice());
        assertNull(expectedRoom.getPhoto());
    }

    @Test
    void canGetAllRoomTypes() {
        // Arrange
        List<String> expectedRoomTypes = Arrays.asList("Single", "Double", "Suite");
        when(roomRepository.findDistinctRoomTypes()).thenReturn(expectedRoomTypes);

        // Call method to get all distinct types of Rooms
        List<String> actualRoomTypes = roomService.getAllRoomTypes();

        // Test if all Room types are retrieved correctly
        assertEquals(expectedRoomTypes, actualRoomTypes);
        verify(roomRepository, times(1)).findDistinctRoomTypes();
    }

    @Test
    void canGetAllRooms() {
        // Create two sample Rooms
        Room room1 = new Room();
        room1.setId(1L);
        Room room2 = new Room();
        room2.setId(2L);
        List<Room> expectedRooms = Arrays.asList(room1, room2);

        // Arrange
        when(roomRepository.findAll()).thenReturn(expectedRooms);

        // Use method to get all distinct Rooms
        List<Room> actualRooms = roomService.getAllRooms();

        // Test if all Rooms are retrieved correctly
        assertEquals(expectedRooms, actualRooms);
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void canGetRoomPhotoByRoomId_whenRoomExistsAndHasPhoto() throws IOException, SQLException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Use method to get photo of a Room
        byte[] expectedPhotoBytes = roomService.getRoomPhotoByRoomId(room.getId());

        // Test if the right photo is retrieved
        assertArrayEquals(file.getBytes(), expectedPhotoBytes);
    }

    @Test
    void throwsResourceNotFoundException_whenRoomDoesNotExist() throws IOException, SQLException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        // Test if the correct error is thrown
        assertThrows(ResourceNotFoundException.class, () -> {
            roomService.getRoomPhotoByRoomId(room.getId());
        });
        verify(roomRepository).findById(room.getId());
    }

    @Test
    void returnsNull_whenRoomExistsButHasNoPhoto() throws SQLException, ResourceNotFoundException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Use method to get photo of a Room
        byte[] expectedPhotoBytes = roomService.getRoomPhotoByRoomId(room.getId());

        // Test to check that there was found no photo
        assertNull(expectedPhotoBytes);
        verify(roomRepository).findById(room.getId());
    }

    @Test
    void canDeleteRoomIfPresent() throws IOException, SQLException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Use method to delete Room
        roomService.deleteRoom(room.getId());

        // Test if Room was deleted successfully
        verify(roomRepository).deleteById(room.getId());
    }

    @Test
    void cannotDeleteRoom_whenRoomDoesNotExist() throws IOException, SQLException {
        // Arrange
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Mocking the repository's behavior to return an empty Optional
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        // Use method to retrieve photo
        roomService.deleteRoom(room.getId());

        // Verify deleteById was never called
        verify(roomRepository, never()).deleteById(room.getId());
    }

    @Test
    void canUpdateRoom_ifRoomIsPresent() throws IOException, SQLException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Use method to update Room
        Room updatedRoom = roomService.updateRoom(1L, "Double", BigDecimal.valueOf(200), file.getBytes());

        // Test if Room was updated correctly
        assertEquals("Double", updatedRoom.getRoomType());
        assertEquals(BigDecimal.valueOf(200), updatedRoom.getRoomPrice());
        assertNotNull(updatedRoom.getPhoto());
        verify(roomRepository).findById(room.getId());
        verify(roomRepository).save(room);
    }

    @Test
    void canUpdateRoom_withoutChangingRoomTypeOrPrice() throws SQLException, IOException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Use method to update Room
        Room updatedRoom = roomService.updateRoom(1L, null, null, file.getBytes());

        // Test if only the photo of the Room was updated
        assertEquals("Single", updatedRoom.getRoomType());
        assertEquals(BigDecimal.valueOf(100), updatedRoom.getRoomPrice());
        assertNotNull(updatedRoom.getPhoto());
        verify(roomRepository).findById(room.getId());
        verify(roomRepository).save(room);
    }

    @Test
    void canUpdateRoom_withNullPhoto() throws SQLException, IOException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Use method to update Room
        Room updatedRoom = roomService.updateRoom(1L, "Double", BigDecimal.valueOf(200), null);

        // Test if only the type and price of the Room were updated
        assertEquals("Double", updatedRoom.getRoomType());
        assertEquals(BigDecimal.valueOf(200), updatedRoom.getRoomPrice());
        assertNull(updatedRoom.getPhoto());
        verify(roomRepository).findById(room.getId());
        verify(roomRepository).save(room);
    }

    @Test
    void canGetRoomById() throws IOException, SQLException {
        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, new SerialBlob(file.getBytes()), null);

        // Arrange
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Use method to get a Room
        Optional<Room> expectedRoom = roomService.getRoomById(room.getId());

        // Test if correct Room was retrieved
        assertTrue(expectedRoom.isPresent());
        assertEquals(room, expectedRoom.get());
        verify(roomRepository).findById(room.getId());
    }

    @Test
    void canGetAvailableRooms_whenRoomsAreAvailable() {
        // Arrange
        LocalDate checkInDate = LocalDate.of(2023, 10, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 10, 5);
        String roomType = "Single";

        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);
        List<Room> availableRooms = Collections.singletonList(room);

        // Arrange
        when(roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType)).thenReturn(availableRooms);

        // Use method to get available rooms
        List<Room> result = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);

        // Test if the available rooms were retrieved correctly
        assertEquals(1, result.size());
        assertEquals(room, result.get(0));
        verify(roomRepository).findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }

    @Test
    void canGetAvailableRooms_whenNoRoomsAreAvailable() {
        // Arrange
        LocalDate checkInDate = LocalDate.of(2023, 10, 1);
        LocalDate checkOutDate = LocalDate.of(2023, 10, 5);
        String roomType = "Single";

        // Create a Room
        room = new Room(1L, "Single", BigDecimal.valueOf(100), false, null, null);
        List<Room> availableRooms = Collections.singletonList(room);

        // Arrange
        when(roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType)).thenReturn(Collections.emptyList());

        // Use method to get available rooms
        List<Room> result = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);

        // Test to see that there are no available rooms
        assertTrue(result.isEmpty());
        verify(roomRepository).findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}