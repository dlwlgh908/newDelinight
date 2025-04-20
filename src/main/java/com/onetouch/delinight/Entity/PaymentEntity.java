/*********************************************************************
 * 클래스명 : PaymentEntity
 * 기능 : 정산
 * 작성자 : 이동건
 * 작성일 : 2025-04-15
 * 수정 : 2025-04-15
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.PayType;
import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToMany
    private List<OrdersEntity> ordersEntityList;

    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    OrderType orderType; // 선결제(PAYNOW) or 후결제(PAYLATER)

    @Enumerated(EnumType.STRING)
    PaidCheck paidCheck;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(name = "total_amount")
    private BigDecimal totalAmount; // 주문 총 금액


}
