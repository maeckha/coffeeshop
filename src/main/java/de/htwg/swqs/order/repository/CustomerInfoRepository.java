package de.htwg.swqs.order.repository;

import de.htwg.swqs.order.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

}
