package hanhuynh.com.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hanhuynh.com.orderservice.entity.OutboxEvent;
import hanhuynh.com.orderservice.enums.OutboxStatus;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {

	/**
	 * get all unpublished events
	 * @param status
	 * @return list of OutboxEvent
	 */
	List<OutboxEvent> findByStatus(OutboxStatus status);
}
