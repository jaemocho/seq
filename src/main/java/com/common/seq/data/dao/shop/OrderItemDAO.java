package com.common.seq.data.dao.shop;

import java.util.List;

import com.common.seq.data.entity.shop.OrderItem;

public interface OrderItemDAO {
    
    public OrderItem save(OrderItem orderItem);

    public List<OrderItem> findByOrderId(Long orderId);

    public void delete(OrderItem orderItem);
}
