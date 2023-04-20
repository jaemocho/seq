package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.repository.BaseRepositoryTest;

public class CategoryRepositoryUnitTest extends BaseRepositoryTest{
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void category_test() {
        
        // category 초기 date insert
        Category category1 = Category.builder()
                                .name("WOMEN")
                                .build();

        
        Category category2 = Category.builder()
                                .name("MEN")
                                .build();        

        Category category3 = Category.builder()
                                .name("KIDS")
                                .build();

        Category category4 = Category.builder()
                                .name("BABY")
                                .build();                                
        

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);

        categoryRepository.flush();

        // 전체 조회 테스트 
        List<Category> categorys = categoryRepository.findAll();
        assertEquals(4, categorys.size());

        
        // 삭제 테스트 
        categoryRepository.delete(category2);
        categorys = categoryRepository.findAll();
        assertEquals(3, categorys.size());

        // category 에 item 추가 후 조회 테스트 
        categorys = categoryRepository.findByName("WOMEN");
        Category womenCategory = categorys.get(0);

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
        
        // item 저장
        itemRepository.save(item1);
        itemRepository.save(item2);

        itemRepository.flush();

        // category 에 item 추가 
        womenCategory.addItem(item1);
        womenCategory.addItem(item2);
        
        categoryRepository.flush();

        categorys = categoryRepository.findByName("WOMEN");
        womenCategory = categorys.get(0);
        assertEquals(2, womenCategory.getItems().size());
    }

}
