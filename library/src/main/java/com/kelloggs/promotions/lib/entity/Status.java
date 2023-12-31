package com.kelloggs.promotions.lib.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Status_Master")
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="status", unique = true, nullable = false)
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
