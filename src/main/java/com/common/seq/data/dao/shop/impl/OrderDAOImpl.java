package com.common.seq.data.dao.shop.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.shop.OrderDAO;
import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.repository.shop.OrderRepository;
import com.common.seq.data.repository.shop.search.OrderSearch;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderDAOImpl implements OrderDAO{
    
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order findOrderInfoByOrderId(Long id) {
        return orderRepository.findOrderInfoByOrderId(id);
    }

    public List<Order> findOrderInfoByMemberId(String id) {
        return orderRepository.findOrderInfoByMemberId(id);
    }

    public List<Order> findByOrderDateLessThan(Date orderDate) {
        return orderRepository.findByOrderDateLessThan(orderDate);
    }

    public List<Order> findByOrderDateGreaterThan(Date orderDate) {
        return orderRepository.findByOrderDateGreaterThan(orderDate);
    }

    public List<Order> findByOrderDateBetween(Date start, Date end) {
        return orderRepository.findByOrderDateBetween(start, end);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch.toSpecification());
    }

}
