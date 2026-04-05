package hanhuynh.com.shipmentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hanhuynh.com.shipmentservice.entity.Shipment;
import hanhuynh.com.shipmentservice.entity.ShipmentFailedLog;

@Repository
public interface ShipmentFailedLogRepository extends JpaRepository<ShipmentFailedLog, String> {

	/**
	 * Retrieve all unprocessed records
	 * @param resolvedStatus
	 * @return list of ShipmentFailedLog
	 */
	List<ShipmentFailedLog> findByResolvedStatus(String resolvedStatus);
	
	/**
	 * find ShipmentFailedLog by orderId
	 * @param orderId
	 * @return ShipmentFailedLog obj
	 */
	ShipmentFailedLog findByOrderId(String orderId);
}
