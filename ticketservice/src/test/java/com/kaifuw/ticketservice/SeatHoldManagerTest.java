package com.kaifuw.ticketservice;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.kaifuw.ticketservice.constants.Message;
import com.kaifuw.ticketservice.dto.Seat;
import com.kaifuw.ticketservice.dto.SeatHold;
import com.kaifuw.ticketservice.dto.Venue;
import com.kaifuw.ticketservice.enums.SeatState;
import com.kaifuw.ticketservice.helpers.SeatHoldManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Venue.class)
public class SeatHoldManagerTest {
    private final SeatHoldManager mgr = new SeatHoldManager();
    private Seat[][] mockSeats;
    private SeatHold mockSeatHold;

    @Before
    public void setup() {
        mockSeats = new Seat[2][4];
        mockSeatHold = new SeatHold(TestData.EMAIL_1);
        for (int i = 0; i < mockSeats.length; i++) {
            for (int j = 0; j < mockSeats[0].length; j++) {
                mockSeats[i][j] = new Seat(i + "-" + j);
            }
        }

        for (int i = 0; i < 2; i++) {
            Seat s = mockSeats[0][i];
            s.setUserEmail(TestData.EMAIL_1);
            s.hold();
            mockSeatHold.addSeat(s.getNumber());
        }
    }

    @Test
    public void testReleaseSeatsHeld() {
        mockStatic(Venue.class);
        expect(Venue.getOneSeat(0, 0)).andReturn(mockSeats[0][0]);
        expect(Venue.getOneSeat(0, 1)).andReturn(mockSeats[0][1]);
        Venue.updateNumAvailable(2);
        expectLastCall().once();
        replay(Venue.class);

        mgr.releaseSeatsHeld(mockSeatHold);

        verify(Venue.class);

        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(SeatState.A, mockSeats[0][i].getState());
            Assert.assertEquals(null, mockSeats[0][i].getUserEmail());
        }

    }

    @Test
    public void testHoldSeats() {
        mockStatic(Venue.class);
        expect(Venue.getSeats()).andReturn(mockSeats);
        Venue.updateNumAvailable(-4);
        expectLastCall().once();
        replay(Venue.class);

        SeatHold sh = new SeatHold(TestData.EMAIL_3);
        mgr.holdSeats(4, TestData.EMAIL_3, sh);

        verify(Venue.class);

        String[] seatNums = new String[] {"0-2","0-3", "1-0", "1-1"};
        for (int i = 0; i < sh.getSeatsList().size(); i++) {
            Assert.assertEquals(seatNums[i], sh.getSeatsList().get(i));
        }
    }

    @Test
    public void testReserveSeats() {
        mockStatic(Venue.class);
        expect(Venue.getOneSeat(0, 0)).andReturn(mockSeats[0][0]);
        expect(Venue.getOneSeat(0, 1)).andReturn(mockSeats[0][1]);
        replay(Venue.class);
        mgr.addToTracker(mockSeatHold);
        mgr.reserveSeats(mockSeatHold.getId(), TestData.EMAIL_1);

        verify(Venue.class);

        for (int i = 0; i < mockSeatHold.getSeatsList().size(); i++) {
            Assert.assertEquals(SeatState.R, mockSeats[0][i].getState());
            Assert.assertEquals(TestData.EMAIL_1, mockSeats[0][i].getUserEmail());
        }
    }

    @Test
    public void testReserveSeatsInvalidEmail() {
        mgr.addToTracker(mockSeatHold);
        String actual = mgr.reserveSeats(mockSeatHold.getId(), TestData.EMAIL_2);
        String expected = String.format(Message.SEATHOLD_NOT_FOUND, TestData.EMAIL_2, mockSeatHold.getId());

        Assert.assertEquals(expected, actual);
    }
}
