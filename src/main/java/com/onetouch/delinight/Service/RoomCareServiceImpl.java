/*********************************************************************
 * 클래스명 : RoomCareServiceImpl
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.RoomCareStatus;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.RoomCareEntity;
import com.onetouch.delinight.Entity.RoomCareItemEntity;
import com.onetouch.delinight.Repository.RoomCareItemRepository;
import com.onetouch.delinight.Repository.RoomCareMenuRepository;
import com.onetouch.delinight.Repository.RoomCareRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class RoomCareServiceImpl implements RoomCareService{

    private final RoomCareRepository roomCareRepository;
    private final RoomCareItemRepository roomCareItemRepository;
    private final ModelMapper modelMapper;
    private final MembersService membersService;
    private final CheckInService checkInService;
    private final HotelService hotelService;
    private final RoomCareMenuRepository roomCareMenuRepository;


    @Override
    public void orders(List<RoomCareItemDTO> roomCareItemDTOList, String email) {
        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(email);
        List<RoomCareItemEntity> roomCareItemEntities = new ArrayList<>();
        for(RoomCareItemDTO roomCareItemDTO : roomCareItemDTOList){
            RoomCareItemEntity roomCareItemEntity = new RoomCareItemEntity();
            roomCareItemEntity.setQuantity(roomCareItemDTO.getQuantity());
            roomCareItemEntity.setRoomCareMenuEntity(roomCareMenuRepository.findById(roomCareItemDTO.getRoomCareMenuDTOId()).get());
            RoomCareItemEntity newItem = roomCareItemRepository.save(roomCareItemEntity);
            roomCareItemEntities.add(newItem);
        }

        RoomCareEntity roomCareEntity = new RoomCareEntity();
        roomCareEntity.setRoomCareItemEntities(roomCareItemEntities);
        roomCareEntity.setCheckInEntity(modelMapper.map(checkInDTO, CheckInEntity.class));
        roomCareEntity.setRoomCareStatus(RoomCareStatus.AWAITING);
        roomCareEntity.setAwaitingTime(LocalDateTime.now());
        roomCareEntity.setHotelEntity(checkInService.findHotelInByEmail(checkInDTO.getId()));

        for(RoomCareItemEntity roomCareItemEntity : roomCareEntity.getRoomCareItemEntities()){
            roomCareItemEntity.setRoomCareEntity(roomCareEntity);
            roomCareItemRepository.save(roomCareItemEntity);
        }
        roomCareRepository.save(roomCareEntity);

    }
}
