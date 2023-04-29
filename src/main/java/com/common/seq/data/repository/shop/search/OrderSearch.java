package com.common.seq.data.repository.shop.search;

import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import static com.common.seq.data.repository.shop.specifications.OrderSpecifications.equalMemberId;
import static com.common.seq.data.repository.shop.specifications.OrderSpecifications.equalOrderState;

import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.data.entity.shop.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderSearch {
    
    private String memberId;

    private OrderState orderState;

    public Specification<Order> toSpecification() {
        return where(equalOrderState(orderState))
                .and(equalMemberId(memberId));
        
    }
}
