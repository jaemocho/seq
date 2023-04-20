package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.repository.BaseRepositoryTest;

public class OrderRepositoryUnitTest extends BaseRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void order_test() throws Exception {
        
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        
        // order 초기 data insert
        Order order1 = Order.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .build();

        Order order2 = Order.builder()
                        .orderDate(dtFormat.parse("20230417"))
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
}
