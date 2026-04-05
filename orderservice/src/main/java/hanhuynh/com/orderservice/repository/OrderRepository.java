package hanhuynh.com.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hanhuynh.com.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	// JpaRepository provide an available save() function to insert order
}
