package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RoutesDaysId generated by hbm2java
 */
@Embeddable
public class RouteDayId implements java.io.Serializable {

    @Column
    private int routeId;

    @Column
    private int dayId;

    public RouteDayId() {
    }

    public RouteDayId(int routeId, int dayId) {
        this.routeId = routeId;
        this.dayId = dayId;
    }

    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getDayId() {
        return this.dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
