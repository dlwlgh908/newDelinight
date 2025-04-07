package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CheckInDTO;

import java.util.List;

public interface CheckInService {

//    public void create(CheckInDTO checkInDTO, String email);
    public void create(CheckInDTO checkInDTO);

    public void create(CheckInDTO checkInDTO, String phone);

    public List<CheckInDTO> list();



}
