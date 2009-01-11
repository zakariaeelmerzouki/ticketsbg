package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RoutesDaysId generated by hbm2java
 */
@Embeddable
public class RouteDayId implements java.io.Serializable {

    private int routeId;

    private int dayId;

    public RouteDayId() {
    }

    public RouteDayId(int routeId, int dayId) {
        this.routeId = routeId;
        this.dayId = dayId;
    }

    @Column(name = "route_id", nullable = false)
    public int getRouteId() {
        return this.routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    @Column(name = "day_id", nullable = false)
    public int getDayId() {
        return this.dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof RouteDayId))
            return false;
        RouteDayId castOther = (RouteDayId) other;

        return (this.getRouteId() == castOther.getRouteId())
                && (this.getDayId() == castOther.getDayId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getRouteId();
        result = 37 * result + this.getDayId();
        return result;
    }

}
