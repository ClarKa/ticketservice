package com.kaifuw.ticketservice.enums;

public enum SeatState {
    R("Reserved"), H("Hold"), A("Available");
    
    String value;
    SeatState(String val) {
        this.value = val;
    }
}
