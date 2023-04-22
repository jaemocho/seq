package com.common.seq.service.shop;

import java.util.List;

import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqOrderDto;
import com.common.seq.data.dto.shop.RespOrderDto;

public interface OrderService  {

    public Long createOrder(ReqOrderDto reqOrderDto) throws ShopException;

    public RespOrderDto getOrderInfoByOrderId(Long orderId) throws ShopException;

    public List<RespOrderDto> getOrderInfoByMemberId(String memberId);

    // 시간 될 때 JPA Specification 적용 
    // public RespOrderDto getOrderByMemberId(String memberId);

    // public RespOrderDto getOrderByOrderState(OrderState orderState);

    public void cancelOrder(Long orderId) throws ShopException;

    public void updateOrderStatus(Long orderId, OrderState orderState) throws ShopException;

}
