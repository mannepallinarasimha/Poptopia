package com.kelloggs.promotions.lib.listeners;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AuditableListener {

    @PrePersist
    void preCreate(Auditable auditable) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        auditable.setCreatedDateTime(now);
        auditable.setModifiedDateTime(now);
    }

    @PreUpdate
    void preUpdate(Auditable auditable) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        auditable.setModifiedDateTime(now);
    }
}
