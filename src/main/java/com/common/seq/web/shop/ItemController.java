package com.common.seq.web.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.data.dto.shop.RespItemDto;
import com.common.seq.service.shop.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class ItemController {
    
    private final ItemService itemService;

    @PostMapping(path ="/item")
    public ResponseEntity<?> createItem(@Valid @RequestBody ReqItemDto reqItemDto) {

        itemService.addItem(reqItemDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity<?> removeItem(@PathVariable("id") Long id) throws ShopException {

        itemService.removeItem(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/item/{id}")
    public ResponseEntity<RespItemDto> getItemById(@PathVariable("id") Long id) throws ShopException{

        return new ResponseEntity<RespItemDto>(itemService.getItemById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/items")
    public ResponseEntity<List<RespItemDto>> getItemByCategoryId(@RequestParam(required = false) Long categoryId) throws ShopException{

        if(categoryId == null) {
            return new ResponseEntity<List<RespItemDto>>(itemService.getAllItem(), HttpStatus.OK);    
        }

        return new ResponseEntity<List<RespItemDto>>(itemService.getItemByCategoryId(categoryId), HttpStatus.OK);
    }

    @PutMapping(path = "/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ReqItemDto reqItemDto) throws ShopException {

        itemService.updateItem(id, reqItemDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    
}
