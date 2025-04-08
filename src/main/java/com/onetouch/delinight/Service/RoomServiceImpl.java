/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.RoomRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{


    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final UsersRepository usersRepository;
    @Override
    public RoomDTO create(RoomDTO roomDTO) {

        RoomEntity roomEntity =
            modelMapper.map(roomDTO, RoomEntity.class);



        HotelEntity hotelEntity =
                hotelRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        UsersEntity usersEntity =
                usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);


        roomEntity.setHotelEntity(hotelEntity);
        roomEntity.setUsersEntity(usersEntity);

        roomRepository.save(roomEntity);
        return roomDTO;
    }

    @Override
    public List<RoomDTO> list() {
        List<RoomEntity> roomEntityList =
            roomRepository.findAll();
        List<RoomDTO> roomDTOList =
                roomEntityList.stream().map(
                        roomEntity -> modelMapper.map(roomEntity, RoomDTO.class)
                                .setHotelDTO(modelMapper.map(roomEntity.getHotelEntity(), HotelDTO.class))).collect(Collectors.toList());
        return roomDTOList;
    }
}
