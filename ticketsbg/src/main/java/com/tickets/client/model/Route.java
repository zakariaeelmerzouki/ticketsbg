package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Routes generated by hbm2java
 */
@Entity
@Table(name = "routes", uniqueConstraints = @UniqueConstraint(columnNames = "firm_id"))
public class Route implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column
    private int firmId;

    @Column
    private int seats;

    @OneToMany(mappedBy="route")
    @OrderBy("hour")
    private Set<RouteHour> routeHours = new HashSet<RouteHour>();

    @OneToMany(mappedBy="route")
    private Set<RouteDay> routeDays = new HashSet<RouteDay>();

    public Route() {
    }

    public Set<RouteHour> getRouteHours(){
        return routeHours;
    }

    public void setRouteHours(Set<RouteHour> routeHours){
        this.routeHours = routeHours;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirmId() {
        return this.firmId;
    }

    public void setFirmId(int firmId) {
        this.firmId = firmId;
    }

    public int getSeats() {
        return this.seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Set<RouteDay> getRouteDays() {
        return routeDays;
    }

    public void setRouteDays(Set<RouteDay> routeDays) {
        this.routeDays = routeDays;
    }

}
