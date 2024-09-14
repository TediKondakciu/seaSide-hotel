package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r where r.roomType LIKE %:roomType% AND " +
            "r.id NOT IN (SELECT bk.room.id FROM Booking bk WHERE ((bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate)))")
    List<Room> findAvailableRoomsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
