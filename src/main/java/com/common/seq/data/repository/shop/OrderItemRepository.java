package com.common.seq.data.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.common.seq.data.entity.shop.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
    @Query(value = "SELECT oi from OrderItem oi join fetch oi.order o WHERE o.id = :orderId")
    List<OrderItem> findByOrderId(Long orderId);
}
