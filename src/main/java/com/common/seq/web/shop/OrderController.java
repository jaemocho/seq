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
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqOrderDto;
import com.common.seq.data.dto.shop.RespOrderDto;
import com.common.seq.service.shop.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class OrderController {

    private final OrderService orderService;

    @PostMapping(path ="/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody ReqOrderDto reqOrderDto) throws ShopException{
        
        Long orderId = orderService.createOrder(reqOrderDto);

        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @DeleteMapping(path="/order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) throws ShopException {

        orderService.cancelOrder(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/order/{id}")
    public ResponseEntity<RespOrderDto> getOrderInfoByOrderId(@PathVariable("id") Long id) throws ShopException {
        
        return new ResponseEntity<RespOrderDto>(orderService.getOrderInfoByOrderId(id), HttpStatus.OK);

    }

    @GetMapping(path = "/order/member/{memberId}")
    public ResponseEntity<List<RespOrderDto>> getOrderInfoByMemberId(@PathVariable("memberId") String memberId) throws ShopException {
        

        return new ResponseEntity<List<RespOrderDto>>(orderService.getOrderInfoByMemberId(memberId), HttpStatus.OK);

    }
    
}
