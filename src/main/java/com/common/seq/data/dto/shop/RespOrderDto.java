package com.common.seq.data.dto.shop;

import java.util.Date;
import java.util.List;

import com.common.seq.common.ShopConstants.OrderState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespOrderDto {
    
    private Long orderId;

    private String memberId;

    private List<OrderItemInfo> orderItemInfos;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class OrderItemInfo {
        
        private Long itemId;

        private String itemName;

        private int itemPrice;
        
        private int itemRequestQty;

        private Long categoryId;

        private String categoryName;
    }

    private Date orderDate;

    private OrderState orderState;

}
