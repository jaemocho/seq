package com.common.seq.data.entity.shop;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "tb_shop_category")
public class Category {

    @Id //PK
    @Column(name = "CATEGORY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //해당 데이터베이스 번호증가 전략을 따라가겠다. 
	private Long id;

    @Column(unique= true, nullable = false)
	private String name;
    
    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<Item>();

    public void addItem(Item item){
        this.items.add(item);

        if(item.getCategory() != this) {
            item.setCategory(this);
        }
    }
}
