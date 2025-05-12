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
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.BranchEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final MembersRepository membersRepository;
    private final InquireRepository inquireRepository;
    private final ImageRepository imageRepository;

    @Override
    public int assignCheck(String email) {
        HotelEntity hotelEntity = hotelRepository.findByMembersEntity_Email(email);
        if (hotelEntity == null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer unansweredCheck(Long hotelId) {
        Integer id = inquireRepository.countByCheckInEntity_RoomEntity_HotelEntity_Id(hotelId);
        return id;
    }

    @Override
    public void addMembers(Long memberId, Long hotelId) {

        HotelEntity hotelEntity = hotelRepository.findById(hotelId).get();
        hotelEntity.setMembersEntity(membersRepository.findById(memberId).get());
        log.info(hotelEntity);
        hotelRepository.save(hotelEntity);
    }

    @Override
    public void create(HotelDTO hotelDTO,String email) {
        HotelEntity hotelEntity =
                modelMapper.map(hotelDTO, HotelEntity.class);
        BranchEntity branchEntity =
                branchRepository.findById(1L).orElseThrow(EntityNotFoundException::new);


        MembersEntity membersEntity =
                membersRepository.findByEmail(email);

        hotelEntity.setMembersEntity(membersEntity);



        hotelEntity.setBranchEntity(branchEntity);
        HotelEntity hotelEntity1 = hotelRepository.save(hotelEntity);
        Long imgNum = hotelDTO.getImgNum(); //imgNum이 null인지 확인하였으나 null값이라 오류
        if (imgNum == null) {
            throw new IllegalArgumentException("이미지 ID(imgNum)가 null입니다.");
        }
        ImageEntity imageEntity = imageRepository.findById(imgNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지가 존재하지 않습니다: " + imgNum));

        imageEntity.setHotelEntity(hotelEntity1);
        imageRepository.save(imageEntity);

    }

    @Override
    public void update(HotelDTO hotelDTO) {


        HotelEntity hotelEntity =
                modelMapper.map(hotelDTO, HotelEntity.class);
        HotelEntity hotel =
                hotelRepository.findById(hotelEntity.getId()).orElseThrow(EntityNotFoundException::new);

        hotel.setName(hotelEntity.getName());
        hotel.setContent(hotelEntity.getContent());
        if (hotelDTO.getImgNum() != null) {
            ImageEntity imageEntity = imageRepository.findById(hotelDTO.getImgNum()).get();
            imageRepository.deleteByHotelEntity_Id(hotelEntity.getId());
            imageEntity.setHotelEntity(hotelEntity);
            imageRepository.save(imageEntity);
        }
        hotelRepository.save(hotel);

    }

    @Override
    public List<HotelDTO> list() {
        List<HotelEntity> hotelEntityList =
                hotelRepository.findAll();
        List<HotelDTO> hotelDTOList =
        hotelEntityList.stream().toList().stream().map(
                hotelEntity -> {
                    HotelDTO hotelDTO = modelMapper.map(hotelEntity, HotelDTO.class);
                    if (hotelEntity.getMembersEntity() != null) {
                        MembersDTO membersDTO = modelMapper.map(hotelEntity.getMembersEntity(), MembersDTO.class);
                        hotelDTO.setMembersDTO(membersDTO);
                    } else {
                        hotelDTO.setMembersDTO(null);
                    }
                    return hotelDTO;
                })
                .collect(Collectors.toList());
        return hotelDTOList;
    }

    @Override
    public HotelDTO findHotelDTOById(Long id) {
        HotelEntity hotelEntity = hotelRepository.findById(id).get();
        HotelDTO hotelDTO = modelMapper.map(hotelEntity, HotelDTO.class);
        return hotelDTO;
    }

    @Override
    public Long findHotelByEmail(String email) {
        HotelEntity hotelEntity = hotelRepository.findByMembersEntity_Email(email);

        return hotelEntity.getId();
    }

    @Override
    public void del(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public void modify(Long memberId, Long hotelId) {
        MembersEntity membersEntity =
                membersRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        HotelEntity hotelEntity =
                hotelRepository.findById(hotelId).orElseThrow(EntityNotFoundException::new);

        hotelEntity.setMembersEntity(membersEntity);
        membersEntity.setHotelEntity(hotelEntity);

        hotelRepository.save(hotelEntity);

    }
}
