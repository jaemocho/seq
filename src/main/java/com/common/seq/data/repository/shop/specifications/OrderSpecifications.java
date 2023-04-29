package com.common.seq.data.repository.shop.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.entity.shop.Order;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class OrderSpecifications {
    
    public static Specification<Order> equalOrderState(final OrderState orderState) {

        return new Specification<Order>() {
            
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                
                if ( orderState == null) return null;

                return builder.equal(root.get("orderState"), orderState);
            }
        };
    }

    public static Specification<Order> equalMemberId(final String memberId) {

        return new Specification<Order>() {
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                
                if ( memberId == null) return null;

                Join<Order, Member> m = root.join("member", JoinType.INNER);

                return builder.equal(m.<String>get("id"), memberId);
            }
        };
    }
}
