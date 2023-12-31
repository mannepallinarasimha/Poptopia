package com.kelloggs.promotions.lib.listeners;

import java.time.LocalDateTime;

public interface Auditable {

    LocalDateTime getCreatedDateTime();

    void setCreatedDateTime(LocalDateTime createdDateTime);

    LocalDateTime getModifiedDateTime();

    void setModifiedDateTime(LocalDateTime modifiedDateTime);
}
