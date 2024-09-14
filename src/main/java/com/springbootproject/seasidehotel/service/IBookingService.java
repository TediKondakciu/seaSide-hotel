package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.model.Booking;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

public interface IBookingService {

    List<Booking> getAllBookingsByRoomId(Long roomId);

    List<Booking> getAllBookings();

    Booking findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, Booking bookingRequest);

    void cancelBooking(Long bookingId);
}
