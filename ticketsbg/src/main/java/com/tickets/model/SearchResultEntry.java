package com.tickets.model;

import java.io.Serializable;
import java.util.Calendar;

import com.tickets.constants.Constants;

public class SearchResultEntry implements Serializable {

    private Run run;
    private Price price;
    private Calendar departureTime;
    private Calendar arrivalTime;

    public SearchResultEntry() {
    }

    public SearchResultEntry(Run run, Price price) {
        super();
        this.run = run;
        this.price = price;
    }

    public Run getRun() {
        return run;
    }
    public void setRun(Run run) {
        this.run = run;
    }
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDuration() {
        return (int) ((arrivalTime.getTimeInMillis() - departureTime
                .getTimeInMillis()) / Constants.ONE_MINUTE);
    }
}
