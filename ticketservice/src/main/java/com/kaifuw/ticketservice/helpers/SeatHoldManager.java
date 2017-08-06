package com.kaifuw.ticketservice.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.kaifuw.ticketservice.constants.Message;
import com.kaifuw.ticketservice.dto.Seat;
import com.kaifuw.ticketservice.dto.SeatHold;
import com.kaifuw.ticketservice.dto.Venue;
import com.kaifuw.ticketservice.enums.SeatState;

public class SeatHoldManager {
    // 20 seconds expiring time.
    private static final long TIME_LIMIT = 20 * 1000;
    private static final long INTERVAL = 2 * 1000;
    private static Logger logger = Logger.getLogger(SeatHoldManager.class);

    private static final Queue<SeatHold> seatHoldQueue = new PriorityQueue<>(new SeatHoldComparator());
    private static boolean running;

    public void addToTracker(SeatHold s) {
        seatHoldQueue.add(s);
    }

    public boolean isRunning() {
        return running;
    }

    public void kickOffTracker() {
        if (running) {
            return;
        }

        running = true;
        logger.info(String.format(Message.TIMER_START));

        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (seatHoldQueue.isEmpty()) {
                    logger.info(String.format(Message.TIMER_CANCEL));
                    running = false;
                    timer.cancel();
                } else {
                    long currTime = System.currentTimeMillis();
                    if (currTime - seatHoldQueue.peek().getTimeHeld() > TIME_LIMIT) {
                        SeatHold sh = seatHoldQueue.poll();
                        logger.info(String.format(Message.HOLD_EXPIRED, sh.getEmail(), sh.getId()));
                        releaseSeatsHeld(sh);
                    }
                }
            }
        };

        timer.schedule(task, 0, INTERVAL);
    }

    public void releaseSeatsHeld(SeatHold seatHold) {
        List<Seat> seatList = seatNumToSeat(seatHold);
        for (Seat seat : seatList) {
            seat.release();
        }

        Venue.updateNumAvailable(seatList.size());
    }

    public void holdSeats(int numSeats, String customerEmail, SeatHold seatHold) {
        int numLeft = numSeats;
        int row = 0;
        Seat[][] seats = Venue.getSeats();
        while (numLeft > 0) {
            for (int col = 0; col < seats[row].length && numLeft > 0; col++) {
                Seat s = seats[row][col];
                if (s.getState() == SeatState.A) {
                    s.setUserEmail(customerEmail);
                    s.hold();

                    seatHold.addSeat(s.getNumber());
                    numLeft--;
                }
            }
            row++;
        }

        String msg = String.format(Message.HOLD_SEATS_SUCCESS, customerEmail, numSeats, seatHold.getId());
        seatHold.setMsg(msg);
        seatHoldQueue.add(seatHold);

        Venue.updateNumAvailable(-numSeats);
        logger.info(msg);
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        Iterator<SeatHold> itr = seatHoldQueue.iterator();
        String msg = null;
        while (itr.hasNext()) {
            SeatHold sh = itr.next();

            if (sh.getId() == seatHoldId && sh.getEmail().equals(customerEmail)) {
                List<Seat> seatList = seatNumToSeat(sh);
                for (Seat seat : seatList) {
                    seat.reserve();
                }

                itr.remove();

                msg = String.format(Message.RESERVE_SEAT_SUCCESS, customerEmail, seatHoldId);
                logger.info(msg);

                break;
            }
        }

        if (msg == null) {
            msg = String.format(Message.SEATHOLD_NOT_FOUND, customerEmail, seatHoldId);
            logger.warn(msg);
        }
        return msg;
    }

    private List<Seat> seatNumToSeat(SeatHold seatHold) {
        List<Seat> list = new ArrayList<>();
        for (String seat : seatHold.getSeatsList()) {
            String[] strs = seat.split("-");
            int row = Integer.parseInt(strs[0]);
            int col = Integer.parseInt(strs[1]);
            list.add(Venue.getOneSeat(row, col));
        }
        return list;
    }
}
