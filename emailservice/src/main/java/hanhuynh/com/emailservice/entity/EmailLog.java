package hanhuynh.com.emailservice.entity;

import java.util.Date;

import hanhuynh.com.emailservice.enums.EmailStatus;
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
@Table(name = "email_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailLog {

	@Id
    @Column(name = "email_id", nullable = false, updatable = false)
    private String emailId;
 
    @Column(name = "order_id", nullable = false)
    private String orderId;
 
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;
 
    @Column(name = "subject", nullable = false)
    private String subject;
 
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;
 
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private EmailStatus status = EmailStatus.SENT;
 
    // Only having value when status is FAILED
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
 
    @Column(name = "sent_date", nullable = false)
    private Date sentDate;
 
    @PrePersist
    protected void onCreate() {
        this.sentDate = new Date();
    }
}
