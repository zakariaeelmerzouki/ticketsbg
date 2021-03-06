package com.tickets.controllers.valueobjects;

public class TicketDisplayInfo implements Comparable<TicketDisplayInfo>{

    private String ticketCode;
    private String seatNumbers;
    private String customerName;

    public String getTicketCode() {
        return ticketCode;
    }
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }
    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public int compareTo(TicketDisplayInfo tdi) {
        if (this.getTicketCode() == null) {
            return -1;
        }

        return this.getTicketCode().compareTo(tdi.getTicketCode());
    }

}
