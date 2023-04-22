package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.Constants.ExceptionClass;
import com.common.seq.common.ShopConstants.OrderState;
import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dao.shop.ItemDAO;
import com.common.seq.data.dao.shop.MemberDAO;
import com.common.seq.data.dao.shop.OrderDAO;
import com.common.seq.data.dao.shop.OrderItemDAO;
import com.common.seq.data.dto.shop.ReqOrderDto;
import com.common.seq.data.dto.shop.RespOrderDto;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.entity.shop.Order;
import com.common.seq.data.entity.shop.OrderItem;
import com.common.seq.service.shop.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService  {
    
    private OrderDAO orderDAO;

    private OrderItemDAO orderItemDAO;

    private ItemDAO itemDAO;

    private MemberDAO memberDAO;

    @Transactional
    public Long createOrder(ReqOrderDto reqOrderDto) throws ShopException {
        
        // 주문자 확인 
        Member orderMember = memberDAO.findById(reqOrderDto.getMemberId());

        if (orderMember == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Member");
        }

        // 주문 생성 
        Order order = Order.builder()
                            .orderDate(new Date())
                            .orderState(OrderState.REQUEST)
                            .build();
        order.setMember(orderMember);                            
        order = orderDAO.save(order);

        Item item = null;
        OrderItem orderItem = null;

        // 주문 item, 수량 확인 후 orderItem 생성 
        for( ReqOrderDto.RequestItem requestItem : reqOrderDto.getRequestItem() ) {

            // item 수량은 공유자 원이기 때문에 for update 
            item = itemDAO.findByIdForUpdate(requestItem.getItemId());

            if ( item == null ) {
                throw new ShopException(ExceptionClass.SHOP
                , HttpStatus.BAD_REQUEST, "Not Found Item");
            }
            
            // 요청 수량 반영
            item.removeRemainQty(requestItem.getRequestQty());
            
            if (item != null) {

                orderItem = OrderItem.builder()
                            .item(item)
                            .build();
                orderItem.setOrder(order);

                orderItemDAO.save(orderItem);
            }
        }

        return order.getId();

    }

    @Transactional(readOnly = true)
    public RespOrderDto getOrderInfoByOrderId(Long orderId) throws ShopException {

        // 상세 정보 필요해서 fetch join 사용 
        Order order = orderDAO.findOrderInfoByOrderId(orderId);

        if (order == null ) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Order");    
        }

        return entityToRespDto(order);
    }

    @Transactional(readOnly = true)
    public List<RespOrderDto> getOrderInfoByMemberId(String memberId) {

        // 상세 정보 필요해서 fetch join 사용 
        List<Order> orders = orderDAO.findOrderInfoByMemberId(memberId);

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

    @Transactional
    public void cancelOrder(Long orderId) throws ShopException {

        // order 는 공유자원이 아니라 for update 없이 
        Order order = orderDAO.findById(orderId);

        if( order == null ) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Order");
        }

        if( !OrderState.REQUEST.equals(order.getOrderState())) {
            
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST
            , "REQUEST 상태일 때만 취소 가능합니다. 관리자에게 문의하세요");
        }

        /// 주문 상태를 cancel로 변경 
        order.updateOrderStatus(OrderState.CANCEL);

        Item item; 
        for ( OrderItem oi : order.getOrderItems()) {
            
            item = itemDAO.findByIdForUpdate(oi.getItem().getId());

            // item 이 없으면 무시하고 진행 
            if ( item == null ) continue;

            // 주문했던 수량 만큼 remainQty에 추가 
            item.addRemainQty(oi.getCount());

        }

    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderState orderState) throws ShopException {
        // order 는 공유자원이 아니라 for update 없이 
        Order order = orderDAO.findById(orderId);

        if( order == null ) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Order");
        }

        order.updateOrderStatus(orderState);
    }
    
}
