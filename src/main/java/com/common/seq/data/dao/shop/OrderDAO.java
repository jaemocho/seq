package com.common.seq.data.dao.shop;

import java.util.Date;
import java.util.List;

import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.repository.shop.search.OrderSearch;

public interface OrderDAO {
    
    public Order save(Order order) ;

    public List<Order> findAll();

    public List<Order> findOrders(OrderSearch orderSearch);

    public Order findById(Long id);

    public Order findOrderInfoByOrderId(Long id);

    public List<Order> findOrderInfoByMemberId(String id);

    public List<Order> findByOrderDateLessThan(Date orderDate);

    public List<Order> findByOrderDateGreaterThan(Date orderDate);

    public List<Order> findByOrderDateBetween(Date start, Date end);
    
}
