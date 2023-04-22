package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.data.entity.shop.Category;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void orderItem_test() throws Exception{

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        
        // order 초기 data insert
        Order order = Order.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();
        
        orderRepository.save(order);
        orderRepository.flush();

        Category category = Category.builder()
                                    .name("MEN")
                                    .build();
        categoryRepository.save(category);
        categoryRepository.flush();

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

        item1.setCategory(category);
        item2.setCategory(category);
        
        itemRepository.save(item1);
        itemRepository.save(item2);

        itemRepository.flush();

        // order 
        // T-shirt 3개 주문 
        // Y-shirt 2개 주문 
        OrderItem orderItem1 = OrderItem.builder()
                                .item(item1)
                                .count(3)
                                .build();

        OrderItem orderItem2 = OrderItem.builder()
                                .item(item2)
                                .count(2)
                                .build();
        
        orderItem1.setOrder(order);
        orderItem2.setOrder(order);
              
        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);

        orderItemRepository.flush();

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertEquals(2, orderItems.size());

        orderItems = orderItemRepository.findByOrderId(order.getId());
        assertEquals(2, orderItems.size());

    }
    
}
