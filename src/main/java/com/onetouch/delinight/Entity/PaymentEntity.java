/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.OrderType;
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




}
