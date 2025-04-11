package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.CheckInStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "guest")
@AllArgsConstructor
@Builder
public class GuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long id;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;


    @Column(name = "reservation_num", nullable = false)
    private String reservationNum;

    @Column(name = "cert_id")
    private String certId;
    @Column(name = "password")
    private String password;






}
