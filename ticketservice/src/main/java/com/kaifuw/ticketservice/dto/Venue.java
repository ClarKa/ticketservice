package com.kaifuw.ticketservice.dto;

public class Venue {
    private final static int ROW_NUM = 10;
    private final static int COL_NUM = 30;
    private final static Seat[][] seats = new Seat[ROW_NUM][COL_NUM];
    private volatile static int numAvailable = ROW_NUM * COL_NUM;

    static {
        for (int row = 0; row < seats.length; row++) {
            for (int col = 0; col < seats[0].length; col++) {
                Seat seat = new Seat(row + "-" + col);
                seats[row][col] = seat;
            }
        }
    }

    public static Seat[][] getSeats() {
        return seats;
    }

    public static Seat getOneSeat(int row, int col) {
        return seats[row][col];
    }

    public static int getNumAvailable() {
        return numAvailable;
    }

    public static void updateNumAvailable(int change) {
        numAvailable += change;
    }
}
