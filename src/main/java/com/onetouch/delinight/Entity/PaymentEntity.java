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
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToMany
    private List<OrdersEntity> ordersEntityList;

    @Enumerated(EnumType.STRING)
    OrderType orderType; // 선결제(PAYNOW) or 후결제(PAYLATER)

    @Column(name="orders_key")
    private String key; //대외용 오더 키 timeStamp+orderid

    @Enumerated(EnumType.STRING)
    PaidCheck paidCheck;


}
