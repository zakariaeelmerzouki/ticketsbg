package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tickets generated by hbm2java
 */
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName="id")
    private User user;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="customerId", referencedColumnName="id")
    private Customer customerInformation;

    @Column(length = 16)
    private String paymentCode;

    @Column(length = 30)
    private String ticketCode;

    @Column
    private BigDecimal price;

    @Column
    private String startStop;

    @Column
    private String endStop;

    @Column
    private boolean isTwoWay;

    @ManyToOne
    @JoinColumn(name="runId", referencedColumnName="runId")
    private Run run;

    @ManyToOne
    @JoinColumn(name="returnRunId", referencedColumnName="runId")
    private Run returnRun;

    @Column
    private boolean committed;

    @Column
    private boolean paymentInProcess;

    @Column
    private PaymentMethod paymentMethod;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationTime;

    @ManyToOne
    private Discount discount;

    @Column
    private int seat;

    @Column
    private int returnSeat;

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentCode() {
        return this.paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getTicketCode() {
        return this.ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStartStop() {
        return startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getEndStop() {
        return endStop;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public boolean isTwoWay() {
        return isTwoWay;
    }

    public void setTwoWay(boolean isTwoWay) {
        this.isTwoWay = isTwoWay;
    }

    public Customer getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(Customer customerInformation) {
        this.customerInformation = customerInformation;
    }

    public boolean isPaymentInProcess() {
        return paymentInProcess;
    }

    public void setPaymentInProcess(boolean paymentInProcess) {
        this.paymentInProcess = paymentInProcess;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Run getReturnRun() {
        return returnRun;
    }

    public void setReturnRun(Run returnRun) {
        this.returnRun = returnRun;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getReturnSeat() {
        return returnSeat;
    }

    public void setReturnSeat(int returnSeat) {
        this.returnSeat = returnSeat;
    }
}
