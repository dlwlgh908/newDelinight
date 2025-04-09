/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {

    public Page<OrdersDTO> processList(Pageable pageable, String email);
    public Page<OrdersDTO> completeList(Pageable pageable, String email);
    public StoreDTO findStoreByADMINEmail(String email);

    public List<OrdersDTO> read(Long paymentNum);

    public void changePayLater(Long ordersId, String memo);
    public void changePayNow(Long ordersId, String memo);

    public boolean pendingCheck(Long paymentId);
    public void changeStatus(Long ordersId, String ordersStatus);
    public OrdersStatus checkStatus(String ordersStatus);

}
