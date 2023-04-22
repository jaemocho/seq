package com.common.seq.data.dao.shop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.shop.OrderItemDAO;
import com.common.seq.data.entity.shop.OrderItem;
import com.common.seq.data.repository.shop.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemDAOImpl implements OrderItemDAO{
    
    private final OrderItemRepository orderItemRepository;

    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public void delete(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }
}
