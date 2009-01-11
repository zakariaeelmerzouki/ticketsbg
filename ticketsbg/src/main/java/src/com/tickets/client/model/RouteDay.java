package com.tickets.client.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RoutesDays generated by hbm2java
 */
@Entity
@Table(name = "routes_days", uniqueConstraints = @UniqueConstraint(columnNames = "day_id"))
public class RouteDay implements java.io.Serializable {

    private RouteDayId id;

    private Route route;

    @ManyToOne
    @JoinColumn(name="route_id", insertable = false, updatable = false)
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public RouteDay() {
    }

    public RouteDay(RouteDayId id) {
        this.id = id;
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "routeId", column = @Column(name = "route_id", nullable = false)),
            @AttributeOverride(name = "dayId", column = @Column(name = "day_id", unique = true, nullable = false)) })
    public RouteDayId getId() {
        return this.id;
    }

    public void setId(RouteDayId id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RouteDay other = (RouteDay) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
