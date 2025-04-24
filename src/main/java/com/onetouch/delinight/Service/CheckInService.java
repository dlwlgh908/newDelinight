package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CheckInService {

    //    public void create(CheckInDTO checkInDTO, String email);
    public void create(RoomEntity roomEntity);


    public List<CheckInDTO> list();

    public List<CheckInDTO> list2();

    public void checkin(CheckInDTO checkInDTO);

    public void checkout(Long id);

    public UsersDTO  checkEmail(String email);

    public List<CheckInDTO> getListCheckinByStatus(CheckInStatus checkInStatus);

    public CheckInDTO findCheckInByEmail(String email);





}
