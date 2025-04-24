/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.OrdersStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {

    private Long id;
    OrderType orderType;
    private  String key;
    private String memo;
    private StoreDTO storeDTO;
    private CheckInDTO checkInDTO;
    private Long totalPrice;
    private List<OrdersItemDTO> ordersItemDTOList;
    private OrdersStatus ordersStatus;
    private CheckOutLogDTO checkOutLogDTO;

    private LocalDateTime pendingTime;
    private LocalDateTime awaitingTime;
    private LocalDateTime preparingTime;
    private LocalDateTime deliveringTime;
    private LocalDateTime deliveredTime;


    public OrdersDTO setStoreDTO(StoreDTO storeDTO){
        this.storeDTO = storeDTO;
        return this;
    }
    public OrdersDTO setCheckInDTO(CheckInDTO checkInDTO){
        this.checkInDTO = checkInDTO;
        return this;
    }

//    public OrdersDTO setCheckOutLogDTO(CheckOutLogDTO checkOutLogDTO) {
//        this.checkOutLogDTO = checkOutLogDTO;
//        return this;
//    }

    public OrdersDTO setOrdersItemDTOList(List<OrdersItemDTO> ordersItemDTOList){
        this.ordersItemDTOList = ordersItemDTOList;
        return this;
    }

    public OrderType orderType(){
        return orderType;
    }

}
