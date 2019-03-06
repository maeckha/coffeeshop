package de.htwg.swqs.order.repository;

import de.htwg.swqs.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
