package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.repository.BaseRepositoryTest;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.entity.shop.OrderItem;

public class OrderRepositoryUnitTest extends BaseRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void order_test() throws Exception {
        
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

        // order 초기 data insert
        Order order1 = Order.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();

        Order order2 = Order.builder()
                        .orderDate(dtFormat.parse("20230417"))
                        .orderState(OrderState.REQUEST)
                        .build();

        orderRepository.save(order1);
        orderRepository.save(order2);

        orderRepository.flush();

        // 전체 조회 테스트
        List<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size());

        // orderDate 로 조회 테스트 LessThan
        orders = orderRepository.findByOrderDateLessThan(dtFormat.parse("20230418"));
        assertEquals(2, orders.size());

        // orderDate 로 조회 테스트 LessThan
        orders = orderRepository.findByOrderDateLessThan(dtFormat.parse("20230417"));
        assertEquals(1, orders.size());

        // orderDate 로 조회 테스트 GreaterThan
        orders = orderRepository.findByOrderDateGreaterThan(dtFormat.parse("20230418"));
        assertEquals(0, orders.size());

        // orderDate 로 조회 테스트 GreaterThan
        orders = orderRepository.findByOrderDateGreaterThan(dtFormat.parse("20230415"));
        assertEquals(2, orders.size());

        // orderDate 로 조회 테스트 between
        orders = orderRepository.findByOrderDateBetween(dtFormat.parse("20230415"), dtFormat.parse("20230419"));
        assertEquals(2, orders.size());

        // orderDate 로 조회 테스트 between
        orders = orderRepository.findByOrderDateBetween(dtFormat.parse("20230415"), dtFormat.parse("20230416"));
        assertEquals(1, orders.size());

    }

    @Test
    public void findByOrderId_test() throws Exception{
        
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

        // member 초기 data insert
        Member member = Member.builder()
                            .id("JJM")
                            .address("수원")
                            .phoneNumber("01111111111")
                            .build();

        memberRepository.save(member);
        memberRepository.flush();

        // order 초기 data insert
        Order order = Order.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();
        
        order.setMember(member);
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
                        .category(category)
                        .build();

        Item item2 = Item.builder()
                        .name("Y-shirt")
                        .price(300)
                        .remainQty(500)
                        .category(category)
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

        Order returnOrder = orderRepository.findOrderInfoByOrderId(order.getId());
        assertEquals(2, returnOrder.getOrderItems().size());
    }
}
