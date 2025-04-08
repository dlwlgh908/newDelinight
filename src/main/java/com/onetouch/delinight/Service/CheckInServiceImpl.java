package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService{

    private final CheckInRepository checkInRepository;
    private final RoomRepository roomRepository;
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutLogRepository checkOutLogRepository;


    @Override
    public void create(CheckInDTO checkInDTO, String email) {

        CheckInEntity checkInEntity =
                modelMapper.map(checkInDTO, CheckInEntity.class)
                        .setUsersEntity(modelMapper.map(checkInDTO.getUsersDTO(), UsersEntity.class));

        RoomEntity roomEntity =
            roomRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        UsersEntity usersEntity =
            usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        GuestEntity guestEntity =
            guestRepository.findById(1L).orElseThrow(EntityNotFoundException::new);




        usersEntity =
            usersRepository.selectEmail(email);


        checkInEntity.builder()
                .checkinDate(checkInEntity.getCheckinDate())
                .checkoutDate(checkInEntity.getCheckoutDate())
                .checkInStatus(checkInEntity.getCheckInStatus())
                .price(checkInEntity.getPrice())
                .guestEntity(checkInEntity.getGuestEntity())
                .guestEntity(guestEntity)
                .usersEntity(usersEntity)
                .roomEntity(roomEntity)
                .build();

        checkInRepository.save(checkInEntity);

    }

    @Override
    public void create(CheckInDTO checkInDTO) {

        CheckInEntity checkInEntity =
                modelMapper.map(checkInDTO, CheckInEntity.class)
                        .setUsersEntity(modelMapper.map(checkInDTO.getUsersDTO(), UsersEntity.class));

        RoomEntity roomEntity =
                roomRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        UsersEntity usersEntity =
                usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        checkInEntity.builder()
                .checkinDate(checkInEntity.getCheckinDate())
                .checkoutDate(checkInEntity.getCheckoutDate())
                .checkInStatus(checkInEntity.getCheckInStatus())
                .price(checkInEntity.getPrice())
                .guestEntity(checkInEntity.getGuestEntity())
                .usersEntity(usersEntity)
                .roomEntity(roomEntity)
                .build();

        checkInRepository.save(checkInEntity);

    }

    @Override
    public List<CheckInDTO> list() {
        List<CheckInEntity> checkInEntityList =
            checkInRepository.findAll();




        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    // getGuestEntity가 null이 아니면 setGuestDTO 호출
                    if (checkInEntity.getGuestEntity() != null) {
                        checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                .setCertPass(checkInEntity.getGuestEntity().getPhone())
                                .setCertNum((int) (Math.random() * 9000) + 1000);

                    }

                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));

                    return checkInDTO;
                }).collect(Collectors.toList());

        return checkInDTOList;
    }


    @Override
    public void checkin(CheckInDTO checkInDTO) {
        CheckInEntity checkInEntity =
            modelMapper.map(checkInDTO, CheckInEntity.class);





        //4자리 난수생성(ID)


        String phone = checkInEntity.getPhone();

        log.info(phone);
        log.info(phone);


        phone = phone.substring(phone.length() - 4);


        log.info(phone);
        log.info(phone);


        log.info("checkin service"+checkInEntity);
        log.info("checkin service"+checkInEntity);
        log.info("checkin service"+checkInEntity);
        log.info("checkin service"+checkInEntity);
        int certNum = (int) (Math.random() * 8999) + 1000;

        checkInDTO.setCertNum(certNum);
        checkInDTO.setCertPass(phone);


        CheckInEntity check =
                checkInRepository.findById(checkInEntity.getId()).orElseThrow(EntityNotFoundException::new);

        check.setCheckinDate(checkInEntity.getCheckinDate());
        check.setCheckoutDate(checkInEntity.getCheckoutDate());
        check.setCheckInStatus(CheckInStatus.CHECKIN);


        GuestEntity guestEntity = new GuestEntity();

        guestEntity.setPhone(checkInDTO.getPhone());
        check.setGuestEntity(guestEntity);
        guestEntity.setEmail("hyo@a.a");
        guestEntity.setReservationNum("2L");

        checkInRepository.save(check);
        guestRepository.save(guestEntity);

    }

    @Override
    public void checkout(Long id) {
        CheckInEntity checkInEntity =
                checkInRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        CheckOutLogEntity checkOutLogEntity = new CheckOutLogEntity();

        checkOutLogEntity.setRoomEntity(checkInEntity.getRoomEntity());
        checkOutLogEntity.setCheckinDate(checkInEntity.getCheckinDate());
        checkOutLogEntity.setCheckoutDate(checkInEntity.getCheckoutDate());
        checkOutLogEntity.setPhone(checkInEntity.getPhone());
        checkOutLogEntity.setPrice(checkInEntity.getPrice());

        checkOutLogRepository.save(checkOutLogEntity);

        checkInEntity.setCheckInStatus(CheckInStatus.VACANCY);
        checkInEntity.setCheckinDate(null);
        checkInEntity.setCheckoutDate(null);
        checkInEntity.setPhone(null);
        checkInEntity.setPrice(0);
        checkInEntity.setGuestEntity(null);

        checkInRepository.save(checkInEntity);

    }
}
