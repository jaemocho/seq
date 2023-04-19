package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.entity.shop.OrderItem;
import com.common.seq.data.repository.BaseRepositoryTest;

public class OrderItemRepositoryUnitTest extends BaseRepositoryTest{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void orderItem_test() throws Exception{

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        
        // order 초기 data insert
        Order order = new Order();
        order.setOrderDate(dtFormat.parse("20230416"));
        
        orderRepository.save(order);
        orderRepository.flush();

        // item 초기 data insert
        Item item1 = new Item();
        item1.setName("T-shirt");
        item1.setPrice(500);
        item1.setRemainQty(500);

        Item item2 = new Item();
        item2.setName("Y-shirt");
        item2.setPrice(300);
        item2.setRemainQty(500);

        
        itemRepository.save(item1);
        itemRepository.save(item2);

        itemRepository.flush();

        // order 
        // T-shirt 3개 주문 
        // Y-shirt 2개 주문 
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(order);
        orderItem1.setItem(item1);
        orderItem1.setCount(3);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(order);
        orderItem2.setItem(item2);
        orderItem2.setCount(2);

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);

        orderItemRepository.flush();

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertEquals(2, orderItems.size());

        orderItems = orderItemRepository.findByOrderId(order.getId());
        assertEquals(2, orderItems.size());

    }
    
}
