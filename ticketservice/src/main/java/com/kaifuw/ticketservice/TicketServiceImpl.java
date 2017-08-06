package com.kaifuw.ticketservice;

import org.apache.log4j.Logger;

import com.kaifuw.ticketservice.constants.Message;
import com.kaifuw.ticketservice.dto.SeatHold;
import com.kaifuw.ticketservice.dto.Venue;
import com.kaifuw.ticketservice.helpers.SeatHoldManager;

public class TicketServiceImpl implements TicketService{
    private static Logger logger = Logger.getLogger(TicketService.class);

    private final SeatHoldManager mgr = new SeatHoldManager();

    @Override
    public int numSeatsAvailable() {
        return Venue.getNumAvailable();
    }

    @Override
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        SeatHold seatHold = new SeatHold(customerEmail);
        if (invalidHoldRequest(numSeats, customerEmail, seatHold)) {
            return seatHold;
        }
        seatHold = mgr.holdSeats(numSeats, customerEmail, seatHold);
        mgr.kickOffTracker();
        return seatHold;
    }

    @Override
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
        String msg = invalidReserveRequest(seatHoldId, customerEmail);
        if (msg != null) {
            return msg;
        }

        return mgr.reserveSeats(seatHoldId, customerEmail);
    }

    public String invalidReserveRequest(int seatHoldId, String customerEmail) {
        String msg = null;
        if (customerEmail == null || customerEmail.isEmpty()) {
            msg = Message.RESERVE_EMPTY_EMAIL;
        } else if (seatHoldId < 0) {
            msg = String.format(Message.INVALID_SEATHOLD_ID, customerEmail, seatHoldId);
        }

        if (msg != null) {
            logger.warn(msg);
        }

        return msg;
    }

    public boolean invalidHoldRequest(int numSeats, String customerEmail, SeatHold seatHold) {
        int avail = Venue.getNumAvailable();
        String msg = null;

        if (customerEmail == null || customerEmail.isEmpty()) {
            msg = Message.HOLD_EMPTY_EMAIL;
        } else if (avail < numSeats) {
            msg = String.format(Message.NOT_ENOUGH_SEATS_AVAILABLE, customerEmail, numSeats, avail);
        } else if (numSeats <= 0) {
            msg = String.format(Message.INVALID_NUMBER_OF_SEATS, customerEmail, numSeats);
        }

        if (msg != null) {
            seatHold.setMsg(msg);
            logger.warn(msg);
            return true;
        }

        return false;
    }
}
