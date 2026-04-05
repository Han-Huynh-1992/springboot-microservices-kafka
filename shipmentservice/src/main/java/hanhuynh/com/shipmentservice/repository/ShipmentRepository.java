package hanhuynh.com.shipmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hanhuynh.com.shipmentservice.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
	
	/**
	 * find shipment logs by orderId
	 * @param orderId
	 * @return Shipment obj
	 */
	Shipment findByOrderId(String orderId);
}
