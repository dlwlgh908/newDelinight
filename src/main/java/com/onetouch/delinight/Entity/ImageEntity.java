/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "ori_img_name")
    private String originImgName;


    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotelEntity;
}
