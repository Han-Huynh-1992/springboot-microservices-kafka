package hanhuynh.com.emailservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hanhuynh.com.emailservice.entity.EmailLog;
import hanhuynh.com.emailservice.enums.EmailStatus;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, String> {

	/**
	 * find email logs by orderId
	 * @param orderId
	 * @return EmailLog obj
	 */
    EmailLog findByOrderId(String orderId);
 
    /**
     * find email logs by status
     * @param status
     * @return list of EmailLog
     */
    List<EmailLog> findByStatus(EmailStatus status);
}
