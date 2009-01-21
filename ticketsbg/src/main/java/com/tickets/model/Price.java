package com.tickets.model;

// Generated 2008-1-20 22:59:52 by Hibernate Tools 3.2.0.b9

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Prices generated by hbm2java
 */
@Entity
@Table(name = "prices")
public class Price implements java.io.Serializable {

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "routeId", column = @Column(name = "route_id", nullable = false)),
            @AttributeOverride(name = "start", column = @Column(name = "start", nullable = false)),
            @AttributeOverride(name = "end", column = @Column(name = "end", nullable = false)) })
    private PricesId id;

    @Column
    private BigDecimal price;

    public Price() {
    }

    public PricesId getId() {
        return this.id;
    }

    public void setId(PricesId id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}