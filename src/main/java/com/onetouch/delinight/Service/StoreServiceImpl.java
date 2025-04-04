/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.StoreRepository;
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
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final MembersRepository membersRepository;

    @Override
    public void create(StoreDTO storeDTO) {
        StoreEntity storeEntity =
            modelMapper.map(storeDTO, StoreEntity.class);
        HotelEntity hotelEntity =
            hotelRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        MembersEntity membersEntity =
            membersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        storeEntity.setHotelEntity(hotelEntity);
        storeEntity.setMembersEntity(membersEntity);

        storeRepository.save(storeEntity);


    }

    @Override
    public List<StoreDTO> list() {
        List<StoreEntity> storeEntityList =
            storeRepository.findAll();
        List<StoreDTO> storeDTOList =
        storeEntityList.stream().toList().stream().map(
                storeEntity -> modelMapper.map(storeEntity, StoreDTO.class)
        ).collect(Collectors.toList());
        return storeDTOList;
    }
}
