package com.kaifuw.ticketservice.constants;

public class Message {
    // Hold
    public static final String NOT_ENOUGH_SEATS_AVAILABLE = "Customer Email: %s. Not enough seats available. Required %d, Currently Available %d.";
    public static final String INVALID_NUMBER_OF_SEATS = "CustomerEmail: %s. Number of seats required is invalid : %d.";
    public static final String HOLD_SEATS_SUCCESS = "Customer Email: %s. %d Seats held. Seat hold id %d.";
    public static final String HOLD_EMPTY_EMAIL = "Email address to hold seats is empty.";

    // Hold Tracker
    public static final String HOLD_EXPIRED = "Customer Email: %s. Seats hold expired.";
    public static final String TIMER_CANCEL = "No seats hold, timer stopped.";

    // Reserve
    public static final String INVALID_SEATHOLD_ID = "CustomerEmail: %s. SeatHold ID to reserve seats is invalid: %d";
    public static final String RESERVE_EMPTY_EMAIL = "Email address to reserve seats is empty.";
    public static final String RESERVE_SEAT_SUCCESS = "Customer Email: %s. Seat hold id: %d reserved.";
    public static final String SEATHOLD_NOT_FOUND = "Customer Email: %s. Seat hold id: %d not found.";
}
