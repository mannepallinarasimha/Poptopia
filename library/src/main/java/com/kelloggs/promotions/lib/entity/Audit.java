package com.kelloggs.promotions.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kelloggs.promotions.lib.listeners.Auditable;
import com.kelloggs.promotions.lib.listeners.AuditableListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, AuditableListener.class})
public abstract class Audit implements Serializable, Auditable {

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date createdDate;

    @Column(name = "modified_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date modifiedDate;

    @Column(name = "created_date_time")
    @JsonIgnore
    private LocalDateTime createdDateTime;

    @Column(name = "modified_date_time")
    @JsonIgnore
    private LocalDateTime modifiedDateTime;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @Override
    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    @Override
    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}
