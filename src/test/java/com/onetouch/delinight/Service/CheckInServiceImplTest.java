package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.GuestRepository;
import com.onetouch.delinight.Repository.RoomRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
    @Autowired
    GuestRepository guestRepository;

    @Test
    public void createTest(){

        CheckInDTO checkInDTO = new CheckInDTO();
        UsersEntity usersEntity =
            usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        UsersDTO usersDTO =
            modelMapper.map(usersEntity, UsersDTO.class);

        checkInDTO.setPrice(10000);
        checkInDTO.setCheckoutDate(LocalDate.now());
        checkInDTO.setCheckinDate(LocalDate.now());
        checkInDTO.setCheckInStatus(CheckInStatus.CHECKIN);
        checkInDTO.setUsersDTO(usersDTO);

        checkInService.create(checkInDTO);


    }
    @Test
    public void test(){
        GuestEntity guestEntity =
            guestRepository.findById(1L).orElseThrow();

        log.info(guestEntity);
        log.info(guestEntity);
        log.info(guestEntity.getPhone());
        log.info(guestEntity.getPhone());

        String num = guestEntity.getPhone();
        String lastfour =
            num.substring(num.length() - 4);
        log.info(lastfour);
        log.info(lastfour);

    }



}