package com.tickets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Stop. The "name" attribute is used as a business-key for comparisons,
 * because it is guaranteed to be unique within one collection
 * (by external factors like validation)
 */
@Entity
@Table(name = "stops")
@NamedQueries({
    @NamedQuery(
            name = "Stop.findStopsByRoute",
            query = "SELECT s FROM Stop s WHERE s.id.route=:route"
    ),
    @NamedQuery(
            name = "Stop.listAllStopNames",
            query = "SELECT s.name FROM Stop s, Price p WHERE p.startStop = s AND p.price > 0 GROUP BY s.name ORDER BY s.name"
    ),
    @NamedQuery(
            name = "Stop.listAllEndStopNames",
            query = "SELECT s.name FROM Stop s, Price p WHERE p.endStop = s AND p.startStop.name LIKE :startStopName AND p.price > 0 GROUP BY s.name ORDER BY s.name"
    ),
    @NamedQuery(
            name = "Stop.listAllStopNamesForFirm",
            query = "SELECT s.name FROM Stop s, Price p WHERE p.startStop = s AND p.route.firm=:firm AND p.price > 0 GROUP BY s.name ORDER BY s.name"
    ),
    @NamedQuery(
            name = "Stop.listAllEndStopNamesForFirm",
            query = "SELECT s.name FROM Stop s, Price p WHERE p.endStop = s AND p.startStop.name LIKE :startStopName AND p.route.firm=:firm AND p.price > 0 GROUP BY s.name ORDER BY s.name"
    ),

    @NamedQuery(
            name = "Stop.listAllStopNamesForUser",
            query = "SELECT s.name FROM Stop s, Price p, User u, Firm firm LEFT OUTER JOIN firm.agents agent " +
                    "WHERE u=:user " +
                    "AND s.route.firm=firm " +
                    "AND p.price > 0 " +
                    "AND (u.firm = firm OR u.agent=agent) " +
                    "AND p.startStop = s " +
                    "GROUP BY s.name ORDER BY s.name"
    ),
    @NamedQuery(
            name = "Stop.listAllEndStopNamesForUser",
            query = "SELECT s.name FROM Stop s, Price p, User u, Firm firm LEFT OUTER JOIN firm.agents agent " +
                    "WHERE u=:user " +
                    "AND s.route.firm=firm " +
                    "AND p.price > 0 " +
                    "AND (u.firm=firm OR u.agent=agent) " +
                    "AND p.endStop = s " +
                    "AND p.startStop.name LIKE :startStopName " +
                    "GROUP BY s.name ORDER BY s.name"
    )
})
public class Stop implements Serializable, Comparable<Stop>, Selectable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int stopId;

    @ManyToOne
    @JoinColumn(name="routeId", nullable=false)
    private Route route;

    /**
     * A column indicating the order of this stop. Starting from 1.
     * See Route.stops for IndexColumn
     */
    @Column
    private int idx;

    @Column
    private String name;

    @Column
    private int timeToArrival;

    @Column
    private int timeToDeparture;

    public Stop() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getTimeToArrival() {
        return timeToArrival;
    }

    public void setTimeToArrival(int timeToArrival) {
        this.timeToArrival = timeToArrival;
    }

    public int getTimeToDeparture() {
        return timeToDeparture;
    }

    public void setTimeToDeparture(int timeToDeparture) {
        this.timeToDeparture = timeToDeparture;
    }


    @Override
    public int compareTo(Stop stop) {
        if (stop.getIdx() > this.getIdx())
            return -1;

        if (stop.getIdx() < this.getIdx())
            return 1;

        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Stop other = (Stop) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    @Override
    public String getLabel() {
        return getName();
    }
}
