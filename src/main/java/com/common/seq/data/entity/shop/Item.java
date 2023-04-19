package com.common.seq.data.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_shop_item")
public class Item {
    
    @Id //PK
    @Column(name = "ITEM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //해당 데이터베이스 번호증가 전략을 따라가겠다. 
	private Long id;

    @Column(unique= false, nullable = false)
    private String name;
    
    @Column(unique= false, nullable = false)
    private int price;

    @Column(name = "remain_qty",  unique= false, nullable = false)
    private int remainQty;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    public void setCategory(Category category) {
        this.category = category;

        if(!category.getItems().contains(this)){
            category.getItems().add(this);
        }
    }

}
