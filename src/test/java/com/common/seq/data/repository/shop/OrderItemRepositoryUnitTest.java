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
        Order order = Order.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .build();
        
        orderRepository.save(order);
        orderRepository.flush();

        // item 초기 data insert
        Item item1 = Item.builder()
                        .name("T-shirt")
                        .price(500)
                        .remainQty(500)
                        .build();

        Item item2 = Item.builder()
                        .name("Y-shirt")
                        .price(300)
                        .remainQty(500)
                        .build();

        
        itemRepository.save(item1);
        itemRepository.save(item2);

        itemRepository.flush();

        // order 
        // T-shirt 3개 주문 
        // Y-shirt 2개 주문 
        OrderItem orderItem1 = OrderItem.builder()
                                .order(order)
                                .item(item1)
                                .count(3)
                                .build();

        OrderItem orderItem2 = OrderItem.builder()
                                .order(order)
                                .item(item2)
                                .count(2)
                                .build();

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);

        orderItemRepository.flush();

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertEquals(2, orderItems.size());

        orderItems = orderItemRepository.findByOrderId(order.getId());
        assertEquals(2, orderItems.size());

    }
    
}
