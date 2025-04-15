/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final MembersRepository membersRepository;
    private final OrdersRepository ordersRepository;
    private final ImageRepository imageRepository;

    @Override
    public Integer awaitingCountCheck(Long storeId) {
        Integer awaitingCount = ordersRepository.countByStoreEntityIdAndOrdersStatus(storeId, OrdersStatus.AWAITING);
        return awaitingCount;
    }

    @Override
    public Long findStoreByEmail(String email) {

        StoreEntity storeEntity = storeRepository.findByMembersEntity_Email(email);
        return storeEntity.getId();

    }
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

    @Override
    public void del(Long id) {
        storeRepository.deleteById(id);
    }

    @Override
    public void update(StoreDTO storeDTO) {

        StoreEntity storeEntity = storeRepository.findById(storeDTO.getId()).get();
        storeEntity.setName(storeDTO.getName());
        storeEntity.setContent(storeDTO.getContent());
        if(storeDTO.getImgNum()!=null){
            ImageEntity imageEntity = imageRepository.findById(storeDTO.getImgNum()).get();
            imageRepository.deleteByStoreEntity_Id(storeEntity.getId());
            imageEntity.setStoreEntity(storeEntity);
            imageRepository.save(imageEntity);
        }
        storeRepository.save(storeEntity);
    }

    @Override
    public StoreDTO read(Long id) {
        Optional<StoreEntity> optionalStoreEntity = storeRepository.findById(id);
        if(optionalStoreEntity.isPresent()){
            StoreEntity storeEntity = optionalStoreEntity.get();
            StoreDTO storeDTO = modelMapper.map(storeEntity, StoreDTO.class);
            return storeDTO;
        }
        else {
            log.info("현재 등록 되어 있는 가맹점이 없습니다.");
            return null;
        }
    }
}
