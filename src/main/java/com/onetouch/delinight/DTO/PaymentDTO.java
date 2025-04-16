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
import com.onetouch.delinight.Constant.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;                        // 결제 식별자

    private String key;                     // 외부에 전달하는 고유 키

    private OrderType orderType;            // 결제 유형 : 선결(PayNow) , 후결(PayLater)

    private PaidCheck paidCheck;            // 결제 완료 여부 : Paid, Unpaid

    private PaymentStatus paymentStatus;    // 결제 상태 : 결제완, 취소 등(Enum)

    private LocalDateTime regTime;          // 결제 생성 시간(BaseTimeEntity 상속 받은 regTime)

}
