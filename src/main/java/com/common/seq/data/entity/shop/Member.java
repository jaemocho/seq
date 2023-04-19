package com.common.seq.data.entity.shop;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_shop_member")
public class Member {
    
    @Id //PK
    @Column(name = "MEMBER_ID")
	private String id;

    @Column(unique = false, nullable = false)
    private String address;

    @Column(name = "phone_number", unique = false, nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<Order>();

    public void addOrders(Order order) {
        this.orders.add(order);

        if(order.getMember() != this) {
            order.setMember(this);
        }
    }

}
