/*********************************************************************
 * 클래스명 : RoomCareServiceImpl
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.RoomCareStatus;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.RoomCareEntity;
import com.onetouch.delinight.Entity.RoomCareItemEntity;
import com.onetouch.delinight.Repository.ImageRepository;
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
public class RoomCareServiceImpl implements RoomCareService {

    private final RoomCareRepository roomCareRepository;
    private final RoomCareItemRepository roomCareItemRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final CheckInService checkInService;
    private final RoomCareMenuRepository roomCareMenuRepository;


    @Override
    public void orders(List<RoomCareItemDTO> roomCareItemDTOList, String email) {
        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(email);
        List<RoomCareItemEntity> roomCareItemEntities = new ArrayList<>();
        for (RoomCareItemDTO roomCareItemDTO : roomCareItemDTOList) {
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

        for (RoomCareItemEntity roomCareItemEntity : roomCareEntity.getRoomCareItemEntities()) {
            roomCareItemEntity.setRoomCareEntity(roomCareEntity);
            roomCareItemRepository.save(roomCareItemEntity);
        }
        roomCareRepository.save(roomCareEntity);

    }

    @Override
    public void changeStatus(Long id) {
        log.info(id);
        RoomCareEntity roomCareEntity = roomCareRepository.findById(id).get();
        roomCareEntity.setRoomCareStatus(RoomCareStatus.DELIVERDE);
        roomCareEntity.setDeliveredTime(LocalDateTime.now());
        roomCareRepository.save(roomCareEntity);
    }

    @Override
    public RoomCareDTO read(Long id) {
        RoomCareEntity roomCareEntity = roomCareRepository.findById(id).get();

        log.info(roomCareEntity);
        RoomCareDTO roomCareDTO = modelMapper.map(roomCareEntity, RoomCareDTO.class)
                .setRoomCareItemDTOList(roomCareEntity.getRoomCareItemEntities().stream().map(
                        roomCareItemEntity -> modelMapper.map(roomCareItemEntity, RoomCareItemDTO.class)
                                .setRoomCareMenuDTO(modelMapper.map(roomCareItemEntity.getRoomCareMenuEntity(), RoomCareMenuDTO.class)
                                        .setImgUrl(imageRepository.findByRoomCareMenuEntity_Id(roomCareItemEntity.getRoomCareMenuEntity().getId()).orElseThrow().getFullUrl())))
                        .toList());

        return roomCareDTO;
    }

    @Override
    public List<RoomCareDTO> list(Long checkInId) {
        List<RoomCareEntity> roomCareEntityList = roomCareRepository.findByCheckInEntity_Id(checkInId);
        List<RoomCareDTO> roomCareDTOList = roomCareEntityList.stream().map(roomCareEntity -> modelMapper.map(roomCareEntity, RoomCareDTO.class)
                .setRoomCareItemDTOList(roomCareEntity.getRoomCareItemEntities().stream().map(roomCareItemEntity -> modelMapper.map(roomCareItemEntity, RoomCareItemDTO.class).setRoomCareMenuDTO(modelMapper.map(roomCareItemEntity.getRoomCareMenuEntity(), RoomCareMenuDTO.class))).toList())).toList();
        return roomCareDTOList;
    }

    @Override
    public List<RoomCareDTO> oldList(Long usersId) {
        List<RoomCareEntity> roomCareEntityList = roomCareRepository.findByCheckOutLogEntity_UsersEntity_Id(usersId);
        List<RoomCareDTO> roomCareDTOList = roomCareEntityList.stream().map(roomCareEntity -> modelMapper.map(roomCareEntity, RoomCareDTO.class)
                .setRoomCareItemDTOList(roomCareEntity.getRoomCareItemEntities().stream().map(roomCareItemEntity -> modelMapper.map(roomCareItemEntity, RoomCareItemDTO.class)).toList())).toList();
        return roomCareDTOList;
    }

    @Override
    public List<RoomCareDTO> showList(Long membersId) {

        List<RoomCareEntity> resultEntities = roomCareRepository.findByCheckInEntity_RoomEntity_HotelEntity_MembersEntity_Id(membersId);
        List<RoomCareDTO> resultDTOS = resultEntities.stream().map(resultEntity ->
                modelMapper.map(resultEntity, RoomCareDTO.class)
                        .setCheckInDTO(modelMapper.map(resultEntity.getCheckInEntity(), CheckInDTO.class)
                                .setRoomDTO(modelMapper.map(resultEntity.getCheckInEntity().getRoomEntity(), RoomDTO.class))
                                ).setRoomCareItemDTOList(resultEntity.getRoomCareItemEntities().stream().map(
                                        roomCareItemEntity -> modelMapper.map(roomCareItemEntity,RoomCareItemDTO.class).setRoomCareMenuDTO(modelMapper.map(roomCareItemEntity.getRoomCareMenuEntity(), RoomCareMenuDTO.class))
                        ).toList())).toList();
        return resultDTOS;
    }
}
