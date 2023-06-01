package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.CommonUtils;
import com.common.seq.common.Constants.ExceptionClass;
import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dao.shop.OrderDAO;
import com.common.seq.data.dao.shop.OrderItemDAO;
import com.common.seq.data.dto.shop.ReqOrderDto;
import com.common.seq.data.dto.shop.RespOrderDto;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.entity.shop.OrderItem;
import com.common.seq.service.shop.ItemService;
import com.common.seq.service.shop.MemberService;
import com.common.seq.service.shop.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService  {
    
    private final OrderDAO orderDAO;

    private final OrderItemDAO orderItemDAO;

    private final MemberService memberService;

    private final ItemService itemService;


    @Transactional
    public Long createOrder(ReqOrderDto reqOrderDto) throws ShopException {
        Member orderMember = memberService.getMember(reqOrderDto.getMemberId());
        CommonUtils.nullCheckAndThrowException(orderMember, Member.class.getName());
        
        Order order = createNewOrder();
        order = orderDAO.save(order);
        order.setMember(orderMember);
        
        addOrderItemToOrder(reqOrderDto, order);
        return order.getId();
    }


    @Transactional(readOnly = true)
    public RespOrderDto getOrderInfoByOrderId(Long orderId) throws ShopException {

        // 상세 정보 필요해서 fetch join 사용 
        Order order = orderDAO.findOrderInfoByOrderId(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        return entityToRespDto(order);
    }

    @Transactional(readOnly = true)
    public List<RespOrderDto> getOrderInfoByMemberId(String memberId) {
        // 상세 정보 필요해서 fetch join 사용 
        return entityToRespDto(orderDAO.findOrderInfoByMemberId(memberId));
    }


    @Transactional
    public void cancelOrder(Long orderId) throws ShopException {
        Order order = orderDAO.findById(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        vaildateOrderStateForCancel(order);
        order.updateOrderStatus(OrderState.CANCEL);
        cancelOrderItem(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderState orderState) throws ShopException {
        // order 는 공유자원이 아니라 for update 없이 
        Order order = orderDAO.findById(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        order.updateOrderStatus(orderState);
    }

    private Order createNewOrder() {
        Order order = Order.builder()
                            .orderDate(new Date())
                            .orderState(OrderState.REQUEST)
                            .build();
        return order;
    }


    private void vaildateOrderStateForCancel(Order order) {
        if( !OrderState.REQUEST.equals(order.getOrderState())) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST
            , "REQUEST 상태일 때만 취소 가능합니다. 관리자에게 문의하세요");
        }
    }

    
    private void addOrderItemToOrder(ReqOrderDto reqOrderDto, Order order) {
        OrderItem orderItem;
        Item item;
        for( ReqOrderDto.RequestItem requestItem : reqOrderDto.getRequestItem() ) {
            item = itemService.getItemForUpdate(requestItem.getItemId());
            CommonUtils.nullCheckAndThrowException(item, Item.class.getName());
            item.removeRemainQty(requestItem.getRequestQty());
            orderItem = createOrderItem(item, requestItem.getRequestQty());
            orderItem.setOrder(order);
            orderItemDAO.save(orderItem); // cascade 로 대체 가능 
        }
    }

    private OrderItem createOrderItem(Item item, int requestQty) {
        OrderItem orderItem = OrderItem.builder()
                                    .item(item)
                                    .count(requestQty)
                                    .build();
        return orderItem;                                    
    }


    private void cancelOrderItem(Order order) {
        Item item; 
        for ( OrderItem oi : order.getOrderItems()) {
            item = itemService.getItemForUpdate(oi.getItem().getId());
            if ( item == null ) continue;
            item.addRemainQty(oi.getCount());
        }
    }

    private List<RespOrderDto> entityToRespDto(List<Order> orders) {
        List<RespOrderDto> respOrderDtos = new ArrayList<RespOrderDto>();

        for( Order o : orders ) {
            respOrderDtos.add(entityToRespDto(o));
        }
        return respOrderDtos;
    }

    private RespOrderDto entityToRespDto(Order order) {

        List<RespOrderDto.OrderItemInfo> orderIteminfos
                 = new ArrayList<RespOrderDto.OrderItemInfo>();
        
        // orderItems resp dto로 반환 
        for( OrderItem o : order.getOrderItems()) {

            orderIteminfos.add(RespOrderDto.OrderItemInfo.builder()
                                        .itemId(o.getItem().getId())
                                        .itemName(o.getItem().getName())
                                        .itemPrice(o.getItem().getPrice())
                                        .itemRequestQty(o.getCount())
                                        .categoryId(o.getItem().getCategory().getId())
                                        .categoryName(o.getItem().getCategory().getName())
                                        .build());
        }

        return RespOrderDto.builder()
                        .orderId(order.getId())
                        .memberId(order.getMember().getId())
                        .orderItemInfos(orderIteminfos)
                        .orderDate(order.getOrderDate())
                        .orderState(order.getOrderState())
                        .build();
                        
    }
    
}
