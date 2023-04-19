package com.common.seq.data.repository.shop;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.shop.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    public List<Order> findByOrderDateLessThan(Date orderDate);

    public List<Order> findByOrderDateGreaterThan(Date orderDate);

    public List<Order> findByOrderDateBetween(Date start, Date end);
}
