package com.kaifuw.ticketservice.dto;

import com.kaifuw.ticketservice.enums.SeatState;

public class Seat {
    private SeatState state;
    private final String number;
    private String userEmail;

    public Seat(String number) {
        this.number = number;
        this.state = SeatState.A;
    }

    /**
     * @return the state
     */
    public SeatState getState() {
        return state;
    }

    /**
     * change seat status to available;
     */
    public void release() {
        this.state = SeatState.A;
        this.userEmail = null;
    }

    /**
     * change seat status to held;
     */
    public void hold() {
        this.state = SeatState.H;
    }

    /**
     * change seat status to reserved;
     */
    public void reserve() {
        this.state = SeatState.R;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
