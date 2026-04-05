package hanhuynh.com.orderservice.entity;

import java.util.Date;

import hanhuynh.com.orderservice.enums.OutboxStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

	@Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
 
    @Column(name = "topic", nullable = false)
    private String topic;
 
    @Column(name = "message_key", nullable = false)
    private String messageKey;
 
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OutboxStatus status = OutboxStatus.PENDING;
 
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;
 
    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }
}
