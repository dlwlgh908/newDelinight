package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.RoomRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2

class CheckInServiceImplTest {

    @Autowired
    CheckInService checkInService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ModelMapper modelMapper;

    @Test
    public void createTest(){

        CheckInDTO checkInDTO = new CheckInDTO();
        UsersEntity usersEntity =
            usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        UsersDTO usersDTO =
            modelMapper.map(usersEntity, UsersDTO.class);

        checkInDTO.setPrice(10000);
        checkInDTO.setCheckoutDate(LocalDateTime.now());
        checkInDTO.setCheckinDate(LocalDateTime.now());
        checkInDTO.setCheckInStatus(CheckInStatus.CHECKIN);
        checkInDTO.setUsersDTO(usersDTO);

        checkInService.create(checkInDTO);









    }

}