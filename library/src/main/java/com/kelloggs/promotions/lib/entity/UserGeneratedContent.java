package com.kelloggs.promotions.lib.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
* Add UserGeneratedContent for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 29th January 2024
*/

@Entity
@Table(name = "user_generated_content")
public class UserGeneratedContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name="ugc_id")
    private Integer id;

    @NotBlank(message = "Name cannot be null or empty")
    @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(100)")
    private String name;

    @NotNull(message = "Start Date cannot be null or empty")
    @Column(name = "start_date", nullable = false)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime start;

    @NotNull(message = "End Date cannot be null or empty")
    @Column(name = "end_date", nullable = false)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime end;

    @NotNull(message = "Created Date cannot be null or empty")
    @Column(name = "created_date", nullable = false)
    //@Temporal(TemporalType.DATE)
    private LocalDateTime created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

}

