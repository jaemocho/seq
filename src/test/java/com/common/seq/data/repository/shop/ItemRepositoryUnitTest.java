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
        Category category1 = new Category();
        category1.setName("WOMEN");

        Category category2= new Category();
        category2.setName("MEN");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        categoryRepository.flush();
        
        // item insert
        Item item1 = new Item();
        item1.setName("women's T shirt");
        item1.setPrice(5000);;
        item1.setRemainQty(0);

        Item item2 = new Item();
        item2.setName("women's T dress");
        item2.setPrice(50000);;
        item2.setRemainQty(50);

        Item item3 = new Item();
        item3.setName("men's T shirt");
        item3.setPrice(55000);;
        item3.setRemainQty(50);

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
        item2.setRemainQty(10000);
        itemRepository.flush();
        assertEquals(10000,itemRepository.findById(item2.getId()).orElse(null).getRemainQty());
        
    }
}
