package com.example.traning.repository;

import com.example.traning.entity.OrderDetails;
import com.example.traning.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {

    List<OrderDetails> findByOrderId(Long orderId);

    Optional<OrderDetails> findByOrderIdAndProductId(Long orderId, Long productId);
}
