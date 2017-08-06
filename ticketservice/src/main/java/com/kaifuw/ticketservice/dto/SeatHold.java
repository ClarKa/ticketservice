package com.kaifuw.ticketservice.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatHold {
    private static final AtomicInteger ATOMICINT = new AtomicInteger();

    private final int id;
    private final String email;
    private final Long timeHeld;
    private final List<String> seatsList;
    private String msg;

    public SeatHold(String email) {
        this.id = ATOMICINT.incrementAndGet();
        this.email = email;
        this.timeHeld = System.currentTimeMillis();
        seatsList = new ArrayList<>();
    }

    /**
     * @return the seatHold Id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the seats list
     */
    public List<String> getSeatsList() {
        return seatsList;
    }

    /**
     * @param the seat num to add to seat hold
     */
    public void addSeat(String seat) {
        seatsList.add(seat);
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the timeHeld
     */
    public Long getTimeHeld() {
        return timeHeld;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
