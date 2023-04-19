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
        Category category1 = new Category();
        category1.setName("WOMEN");

        Category category2= new Category();
        category2.setName("MEN");

        Category category3 = new Category();
        category3.setName("KIDS");

        Category category4 = new Category();
        category4.setName("BABY");

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
        Category womenCategory = categoryRepository.findByName("WOMEN");

        // item insert
        Item item1 = new Item();
        item1.setName("women's T shirt");
        item1.setPrice(5000);;
        item1.setRemainQty(0);

        Item item2 = new Item();
        item2.setName("women's T dress");
        item2.setPrice(50000);;
        item2.setRemainQty(50);
        
        // item 저장
        itemRepository.save(item1);
        itemRepository.save(item2);

        itemRepository.flush();

        // category 에 item 추가 
        womenCategory.addItem(item1);
        womenCategory.addItem(item2);
        
        categoryRepository.flush();

        womenCategory = categoryRepository.findByName("WOMEN");
        assertEquals(2, womenCategory.getItems().size());
    }

}
