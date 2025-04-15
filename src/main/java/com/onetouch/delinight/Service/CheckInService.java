package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.GuestDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CheckInService {

//    public void create(CheckInDTO checkInDTO, String email);
    public void create(RoomEntity roomEntity);


    public List<CheckInDTO> list();

    public void checkin(CheckInDTO checkInDTO);

    public void checkout(Long id);

    public UsersDTO  checkEmail(String email);

    public GuestDTO checkGuest(String password);





}
