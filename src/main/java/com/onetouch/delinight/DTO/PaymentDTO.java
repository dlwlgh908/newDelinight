/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;
    private OrderType orderType;
    private PaidCheck paidCheck;
    private List<OrdersDTO> ordersDTOList;

    public PaymentDTO setOrdersDTOList(List<OrdersDTO> ordersDTOList){
        this.ordersDTOList = ordersDTOList;
        return this;
    }

}
