package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.repository.BaseRepositoryTest;

public class ItemRepositoryUnitTest  extends BaseRepositoryTest {
       
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void category_test() {

        // category 초기 값 insert
        Category category1 = Category.builder()
                                .name("WOMEN")
                                .build();

        
        Category category2 = Category.builder()
                                .name("MEN")
                                .build();     

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        categoryRepository.flush();
        
        // item insert
        Item item1 = Item.builder()
                        .name("women's T shirt")
                        .price(5000)
                        .remainQty(0)
                        .build();

        Item item2 = Item.builder()
                        .name("women's T dress")
                        .price(50000)
                        .remainQty(50)
                        .build();  

        Item item3 = Item.builder()
                        .name("men's T dress")
                        .price(55000)
                        .remainQty(50)
                        .build();  


        // item의 category 지정
        item1.setCategory(category1);
        item2.setCategory(category1);
        item3.setCategory(category2);
        
        // item 저장
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        itemRepository.flush();

        // women's T shirt item 조회
        List<Item> items = itemRepository.findByName("women's T shirt");

        // 조회 확인
        assertEquals(1, items.size());
        assertEquals("WOMEN", items.get(0).getCategory().getName());
        
        // WOMEN category id로 item 조회 
        items = itemRepository.findByCategoryId(category1.getId());
        assertEquals(2, items.size());

        // 45000 원 보다 비싼 item  조회
        items = itemRepository.findByPriceGreaterThan(45000);
        assertEquals(2, items.size());

        // 4500 원 보다 비싼 item  조회
        items = itemRepository.findByPriceGreaterThan(4500);
        assertEquals(3, items.size());

        // 재고가 0개 보다 많은 item 조회 
        items = itemRepository.findByRemainQtyGreaterThan(0);
        assertEquals(2, items.size());

        // qty update test 
        item2.updateItem(item2.getName(), item2.getPrice(), 10000);
        itemRepository.flush();
        assertEquals(10000,itemRepository.findById(item2.getId()).orElse(null).getRemainQty());
        
    }

    @Test
    public void itemUpdate_test() {
        
        // 초기 item data insert
        Item item = Item.builder()
                        .name("T shirt")
                        .price(5000)
                        .remainQty(0)
                        .build();

        itemRepository.save(item);
        itemRepository.flush();

        // select for update 
        /* Hibernate: 
        select
            i1_0.ITEM_ID,
            i1_0.CATEGORY_ID,
            i1_0.name,
            i1_0.price,
            i1_0.remain_qty 
        from
            tb_shop_item i1_0 
        where
            i1_0.ITEM_ID=? for update */
        Item updateItem = itemRepository.findByIdForUpdate(item.getId());

        // 가격 변경
        updateItem.updateItem(updateItem.getName(), 500, updateItem.getRemainQty());;
        itemRepository.flush();

        item = itemRepository.findById(item.getId()).orElse(null);

        assertEquals(500, item.getPrice());

    }
}
