package com.common.seq.data.repository.shop;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.common.seq.data.entity.shop.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    public List<Order> findByOrderDateLessThan(Date orderDate);

    public List<Order> findByOrderDateGreaterThan(Date orderDate);

    public List<Order> findByOrderDateBetween(Date start, Date end);

    @Query(value = "SELECT o from Order o join fetch o.member m join fetch o.orderItems oi join fetch oi.item i  join fetch i.category WHERE o.id = :orderId")
    Order findOrderInfoByOrderId(Long orderId);

    @Query(value = "SELECT o from Order o join fetch o.member m join fetch o.orderItems oi join fetch oi.item i  join fetch i.category WHERE m.id = :memberId")
    List<Order> findOrderInfoByMemberId(String memberId);
    
}
