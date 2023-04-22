package com.common.seq.service.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.data.dao.shop.CategoryDAO;
import com.common.seq.data.dao.shop.ItemDAO;
import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.data.dto.shop.RespItemDto;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.service.shop.impl.ItemServiceImpl;

@ExtendWith(SpringExtension.class)
public class ItemServiceUnitTest {
    
    @Mock
    private ItemDAO itemDAO;

    @Mock
    private CategoryDAO categoryDAO;
    
    @InjectMocks
	private ItemServiceImpl itemService;

    @Test
    public void addItem_test() {

        //given
        ReqItemDto reqItemDto = ReqItemDto.builder()
                                        .name("T-shirt")
                                        .price(5000)
                                        .remainQty(100)
                                        .categoryId(1L)
                                        .build();

        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item = Item.builder()
                    .id(1L)
                    .name("T-shirt")
                    .price(5000)
                    .remainQty(100)
                    .build();


        when(categoryDAO.findById(reqItemDto.getCategoryId())).thenReturn(category);
        when(itemDAO.save(any(Item.class))).thenReturn(item);

        //when
        Item returnitem = itemService.addItem(reqItemDto);

        //then 
        assertEquals("WOMEN", returnitem.getCategory().getName());
    }

    @Test
    public void getAllItem_test() {
        
        //given
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        Item item2 = Item.builder()
                        .id(2L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .build();

        Item item3 = Item.builder()
                        .id(3L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        item1.setCategory(category);
        item3.setCategory(category);
                                
        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        when(itemDAO.findAll()).thenReturn(items);

        // when
        List<RespItemDto> respItemDtos = itemService.getAllItem();

        //then 
        assertEquals(3, respItemDtos.size());

        int categoryCnt = 0;
        for(RespItemDto dto : respItemDtos) {
            if ("WOMEN".equals(dto.getCategoryName())) {
                categoryCnt++;
            }
        }

        assertEquals(2, categoryCnt);
    }

    @Test
    public void getItemByCategoryId_test() {
        
        //given
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();
        
        Item item2 = Item.builder()
                        .id(2L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();
        
        Item item3 = Item.builder()
                        .id(3L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        when(itemDAO.findByCategoryId(1L)).thenReturn(items);

        // when
        List<RespItemDto> respItemDtos = itemService.getItemByCategoryId(1L);

        //then 
        assertEquals(3, respItemDtos.size());

        int categoryCnt = 0;
        for(RespItemDto dto : respItemDtos) {
            if ("WOMEN".equals(dto.getCategoryName())) {
                categoryCnt++;
            }
        }

        assertEquals(3, categoryCnt);
    }

    @Test
    public void updateItem_test() {
        
        // given
        ReqItemDto reqItemDto = ReqItemDto.builder()
                                    .name("T-shirt")
                                    .price(400)
                                    .remainQty(200)
                                    .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .build();
        

        when(itemDAO.findByIdForUpdate(item1.getId())).thenReturn(item1);

        // when 
        itemService.updateItem(item1.getId(), reqItemDto);

        // then
        assertEquals(400, item1.getPrice());
        assertEquals(200, item1.getRemainQty());
        


    }
}
