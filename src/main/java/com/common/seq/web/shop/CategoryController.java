package com.common.seq.web.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.common.seq.data.dto.shop.RespCategoryDto;
import com.common.seq.service.shop.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class CategoryController {
    
    private final CategoryService categoryService;

    @PostMapping(path = "/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody ReqCategoryDto reqCategoryDto) {
        
        categoryService.addCategory(reqCategoryDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/category/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable("id") Long id) throws ShopException {

        categoryService.removeCategory(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/category/{id}")
    public ResponseEntity<RespCategoryDto> getCategoryById(@PathVariable("id") Long id) throws ShopException{

        return new ResponseEntity<RespCategoryDto>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/categorys")
    public ResponseEntity<List<RespCategoryDto>> getCategoryByName(@RequestParam(required = false) String name) throws ShopException{

        if ( name == null || "".equals(name)) {
            return new ResponseEntity<List<RespCategoryDto>>(categoryService.getAllCategory(), HttpStatus.OK);
        }

        return new ResponseEntity<List<RespCategoryDto>>(categoryService.getCategoryByName(name), HttpStatus.OK);
    }

}
