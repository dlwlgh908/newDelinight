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
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService{

    private final CheckInRepository checkInRepository;
    private  final OrdersService ordersService;
    private final RoomRepository roomRepository;
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutLogRepository checkOutLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Override
    public void create(RoomEntity roomEntity) {

        CheckInEntity checkInEntity = CheckInEntity.builder().roomEntity(roomEntity).checkInStatus(CheckInStatus.VACANCY).build();
        checkInRepository.save(checkInEntity);


    }
//
//    @Override
//    public void create(CheckInDTO checkInDTO) {
//
//        CheckInEntity checkInEntity =
//                modelMapper.map(checkInDTO, CheckInEntity.class)
//                        .setUsersEntity(modelMapper.map(checkInDTO.getUsersDTO(), UsersEntity.class));
//
//        RoomEntity roomEntity =
//                roomRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//        UsersEntity usersEntity =
//                usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//
//        checkInEntity.builder()
//                .checkinDate(checkInEntity.getCheckinDate())
//                .checkoutDate(checkInEntity.getCheckoutDate())
//                .checkInStatus(checkInEntity.getCheckInStatus())
//                .price(checkInEntity.getPrice())
//                .guestEntity(checkInEntity.getGuestEntity())
//                .usersEntity(usersEntity)
//                .roomEntity(roomEntity)
//                .build();
//
//        checkInRepository.save(checkInEntity);
//
//    }


    @Override
    public List<CheckInDTO> list2() {
        List<CheckInEntity> checkInEntityList =
                checkInRepository.findAll();
        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
                    }
                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));
                    return checkInDTO;
                }).collect(Collectors.toList());
        return checkInDTOList;
    }

    @Override
    public List<CheckInDTO> list() {
        List<CheckInEntity> checkInEntityList =
                checkInRepository.findAll();




        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
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




        log.info("checkin service"+checkInEntity);
        if(checkInDTO.getUserId()==null) {
            int certNum = (int) (Math.random() * 8999) + 1000;
            checkInDTO.setPassword(String.valueOf(certNum));
        }

        CheckInEntity check =
                checkInRepository.findById(checkInEntity.getId()).orElseThrow(EntityNotFoundException::new);

        check.setPassword(checkInDTO.getPassword());
        check.setCheckinDate(checkInEntity.getCheckinDate());
        check.setCheckoutDate(checkInEntity.getCheckoutDate());


        check.setCheckInStatus(CheckInStatus.CHECKIN);

        if (checkInDTO.getUserId() == null) {
            GuestEntity guestEntity = new GuestEntity();
            guestEntity.setPhone(checkInDTO.getPhone());
            guestEntity.setEmail(checkInDTO.getEmail());
            guestEntity.setCertId(check.getPassword());
            guestEntity.setPassword(passwordEncoder.encode(check.getPassword()));

            check.setGuestEntity(guestEntity);

            String reservationNum = check.getRoomEntity().getId()+"/"+check.getCheckinDate()+"/";
            guestEntity.setReservationNum(reservationNum);
            checkInRepository.save(check);
            guestRepository.save(guestEntity);
            cartService.makeCart(2, guestEntity.getId());


        } else {
            UsersEntity usersEntity =
                    usersRepository.findById(checkInDTO.getUserId()).orElseThrow(EntityNotFoundException::new);
            check.setUsersEntity(usersEntity);
            cartService.makeCart(1, usersEntity.getId());

            checkInRepository.save(check);

        }


    }

    @Override
    public void checkout(Long id) {
        CheckInEntity checkInEntity =
                checkInRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(checkInEntity.getGuestEntity()!=null){
            cartService.deleteCart(2,checkInEntity.getGuestEntity().getId());
        }
        else {
            cartService.deleteCart(1,checkInEntity.getUsersEntity().getId());
        }

        CheckOutLogEntity checkOutLogEntity = new CheckOutLogEntity();


        checkOutLogEntity.setRoomEntity(checkInEntity.getRoomEntity());
        checkOutLogEntity.setCheckinDate(checkInEntity.getCheckinDate());
        checkOutLogEntity.setCheckoutDate(checkInEntity.getCheckoutDate());
        checkOutLogEntity.setPhone(checkInEntity.getPhone());
        checkOutLogEntity.setPrice(checkInEntity.getPrice());
        checkOutLogEntity.setUsersEntity(checkInEntity.getUsersEntity());
        checkOutLogEntity.setGuestEntity(checkInEntity.getGuestEntity());

        checkOutLogRepository.save(checkOutLogEntity);

        checkInEntity.setCheckInStatus(CheckInStatus.VACANCY);
        checkInEntity.setCheckinDate(null);
        checkInEntity.setCheckoutDate(null);
        checkInEntity.setPhone(null);
        checkInEntity.setPrice(0);
        checkInEntity.setGuestEntity(null);
        checkInEntity.setUsersEntity(null);
        checkInEntity.setPassword(null);

        checkInRepository.save(checkInEntity);
        ordersService.checkInToCheckOut(checkInEntity.getId(), checkOutLogEntity.getId());


    }

    @Override
    public UsersDTO checkEmail(String email) {
        UsersEntity usersEntity =
                usersRepository.selectEmail(email);

        UsersDTO usersDTO =
                modelMapper.map(usersEntity, UsersDTO.class);


        return usersDTO;

    }

    @Override
    public CheckInDTO findCheckInByEmail(String email) {
        CheckInEntity checkInEntity = checkInRepository.findByUsersEntity_Email(email);
        CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class).setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class).setHotelDTO(modelMapper.map(checkInEntity.getRoomEntity().getHotelEntity(),HotelDTO.class)));
        return checkInDTO;
    }

    @Override
    public List<CheckInDTO> getListCheckinByStatus(CheckInStatus checkInStatus) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        List<CheckInEntity> checkInEntityList = checkInRepository.selectCheckByStatus(checkInStatus);

        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
                    }
                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));
                    return checkInDTO;
                }).collect(Collectors.toList());
        return checkInDTOList;
    }
}
