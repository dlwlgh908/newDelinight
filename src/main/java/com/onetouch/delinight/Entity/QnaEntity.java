/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "qna")
public class QnaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(length = 50,nullable = false) //컬럼길이, null허용여부
    private String title;
    @Column(length = 2000, nullable = true)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "checkin_id")
    private CheckInEntity checkInEntity;





}
