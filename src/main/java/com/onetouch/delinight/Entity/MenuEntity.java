/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 : MenuEntity 항목 수정(price,stockNumber, MenuStatus 추가)
 * 작성자 : 이효찬.
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.MenuStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String content;

    @Column(nullable = false)
    private String price;

    @Column(name = "stock_number",nullable = false)
    private String stockNumber;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;






}
