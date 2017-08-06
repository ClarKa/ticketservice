package com.kaifuw.ticketservice.helpers;

import java.util.Comparator;

import com.kaifuw.ticketservice.dto.SeatHold;

public class SeatHoldComparator implements Comparator<SeatHold>{
    @Override
    public int compare(SeatHold s1, SeatHold s2) {
        if (s1.getTimeHeld() == s1.getTimeHeld()) {
            return 0;
        } else {
            return s1.getTimeHeld() < s2.getTimeHeld() ? -1 : 1;
        }
    }
}
