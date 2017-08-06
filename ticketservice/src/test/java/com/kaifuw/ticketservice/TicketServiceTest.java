package com.kaifuw.ticketservice;

import org.junit.Assert;
import org.junit.Test;

import com.kaifuw.ticketservice.constants.Message;
import com.kaifuw.ticketservice.dto.SeatHold;
import com.kaifuw.ticketservice.dto.Venue;
import com.kaifuw.ticketservice.enums.SeatState;

public class TicketServiceTest {
    private TicketServiceImpl service = new TicketServiceImpl();

    @Test
    public void testServices() throws InterruptedException {
        // hold sh1 of 4 seats;
        SeatHold sh1 = service.findAndHoldSeats(4, TestData.EMAIL_1);
        for (int i = 0; i < sh1.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.H, Venue.getOneSeat(0, i).getState());
            Assert.assertEquals(TestData.EMAIL_1, Venue.getOneSeat(0, i).getUserEmail());
        }
        Assert.assertEquals(TestData.MAX_SEATS - 4, Venue.getNumAvailable());

        Thread.sleep(5 * 1000);

        // hold sh2 of 2 seats;
        SeatHold sh2 = service.findAndHoldSeats(2, TestData.EMAIL_2);
        for (int i = 4; i < 4 + sh2.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.H, Venue.getOneSeat(0, i).getState());
            Assert.assertEquals(TestData.EMAIL_2, Venue.getOneSeat(0, i).getUserEmail());
        }
        Assert.assertEquals(TestData.MAX_SEATS - 6, Venue.getNumAvailable());

        // reserve sh1.
        service.reserveSeats(sh1.getId(), TestData.EMAIL_1);
        for (int i = 0; i < sh1.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.R, Venue.getOneSeat(0, i).getState());
            Assert.assertEquals(TestData.EMAIL_1, Venue.getOneSeat(0, i).getUserEmail());
        }
        Assert.assertEquals(TestData.MAX_SEATS - 6, Venue.getNumAvailable());

        Thread.sleep(25 * 1000);

        // sh1 reserved.
        for (int i = 0; i < sh1.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.R, Venue.getOneSeat(0, i).getState());
            Assert.assertEquals(TestData.EMAIL_1, Venue.getOneSeat(0, i).getUserEmail());
        }

        // sh2 expired.
        for (int i = 4; i < 4 + sh2.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.A, Venue.getOneSeat(0, i).getState());
            Assert.assertEquals(null, Venue.getOneSeat(0, i).getUserEmail());
        }
        Assert.assertEquals(TestData.MAX_SEATS - 4, Venue.getNumAvailable());
    }

    @Test
    public void testNumSeatsNotValid() {
        SeatHold seatHold = new SeatHold(TestData.EMAIL_1);
        Assert.assertTrue(service.invalidHoldRequest(-1, TestData.EMAIL_1, seatHold));
        Assert.assertEquals(String.format(Message.INVALID_NUMBER_OF_SEATS, TestData.EMAIL_1, -1),seatHold.getMsg());
    }

    @Test
    public void testNotEnoughSeats() {
        SeatHold seatHold = new SeatHold(TestData.EMAIL_1);
        int seatNum = TestData.MAX_SEATS + 1;
        Assert.assertTrue(service.invalidHoldRequest(seatNum, TestData.EMAIL_1, seatHold));

        String expect = String.format(Message.NOT_ENOUGH_SEATS_AVAILABLE, TestData.EMAIL_1, seatNum, TestData.MAX_SEATS);
        String actual = seatHold.getMsg();
        Assert.assertEquals(expect, actual);
    }

    @Test
    public void testEmptyEmail() {
        SeatHold seatHold = new SeatHold(TestData.EMAIL_1);
        int seatNum = TestData.MAX_SEATS + 1;
        Assert.assertTrue(service.invalidHoldRequest(seatNum, "", seatHold));
        Assert.assertEquals(Message.HOLD_EMPTY_EMAIL, seatHold.getMsg());
    }

    @Test
    public void testReserveEmptyEmail() {
        Assert.assertEquals(Message.RESERVE_EMPTY_EMAIL, service.invalidReserveRequest(1, ""));
        Assert.assertEquals(Message.RESERVE_EMPTY_EMAIL, service.invalidReserveRequest(1, null));
        Assert.assertNull(service.invalidReserveRequest(1, TestData.EMAIL_1));
    }

    @Test
    public void testReserveInvalidID() {
        String actual = service.invalidReserveRequest(-1, TestData.EMAIL_1);
        String expect = String.format(Message.INVALID_SEATHOLD_ID, TestData.EMAIL_1, -1);
        Assert.assertEquals(expect, actual);
    }
}
