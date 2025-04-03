/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.Entity.BranchEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Repository.BranchRepository;
import com.onetouch.delinight.Repository.HotelRepository;
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
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    @Override
    public void create(HotelDTO hotelDTO) {
        HotelEntity hotelEntity =
            modelMapper.map(hotelDTO, HotelEntity.class);
        BranchEntity branchEntity =
                branchRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        hotelEntity.setBranchEntity(branchEntity);
        hotelRepository.save(hotelEntity);

    }

    @Override
    public List<HotelDTO> list() {
        List<HotelEntity> hotelEntityList =
            hotelRepository.findAll();
        List<HotelDTO> hotelDTOList =
        hotelEntityList.stream().toList().stream().map(
                hotelEntity -> modelMapper.map(hotelEntity, HotelDTO.class)
        ).collect(Collectors.toList());
        return hotelDTOList;
    }
}
