package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Stops generated by hbm2java
 */
@Entity
@Table(name = "stops")
public class Stop implements java.io.Serializable {

    private StopsId id;

    private int arrivalTime;

    private int departureTime;

    public Stop() {
    }

    public Stop(StopsId id, int arrivalHour, int departureHour) {
        this.id = id;
        this.arrivalTime = arrivalHour;
        this.departureTime = departureHour;
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "routeId", column = @Column(name = "route_id", nullable = false)),
            @AttributeOverride(name = "idx", column = @Column(name = "idx", nullable = false)) })
    public StopsId getId() {
        return this.id;
    }

    public void setId(StopsId id) {
        this.id = id;
    }

    @Column(name = "arrival_hour", nullable = false)
    public int getArrivalHour() {
        return this.arrivalTime;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalTime = arrivalHour;
    }

    @Column(name = "departure_hour", nullable = false)
    public int getDepartureHour() {
        return this.departureTime;
    }

    public void setDepartureHour(int departureHour) {
        this.departureTime = departureHour;
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
        final Stop other = (Stop) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
