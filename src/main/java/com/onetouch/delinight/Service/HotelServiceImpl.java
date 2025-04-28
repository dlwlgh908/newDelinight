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
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.BranchRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final MembersRepository membersRepository;
    private final InquireRepository inquireRepository;

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
    public void create(HotelDTO hotelDTO, String email) {
        HotelEntity hotelEntity =
            modelMapper.map(hotelDTO, HotelEntity.class);
        BranchEntity branchEntity =
                branchRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        MembersEntity membersEntity =
            membersRepository.findByEmail(email);

        hotelEntity.setMembersEntity(membersEntity);



        hotelEntity.setBranchEntity(branchEntity);
        hotelRepository.save(hotelEntity);

    }

    @Override
    public void update(HotelDTO hotelDTO) {


        HotelEntity hotelEntity =
            modelMapper.map(hotelDTO, HotelEntity.class);
        HotelEntity hotel =
                hotelRepository.findById(hotelEntity.getId()).orElseThrow(EntityNotFoundException::new);

        hotel.setName(hotelEntity.getName());
        hotel.setContent(hotelEntity.getContent());

    }

    @Override
    public List<HotelDTO> list() {
        List<HotelEntity> hotelEntityList =
            hotelRepository.findAll();
        List<HotelDTO> hotelDTOList =
        hotelEntityList.stream().toList().stream().map(hotelEntity -> {
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
    public Long findHotelByEmail(String email) {
        HotelEntity hotelEntity = hotelRepository.findByMembersEntity_Email(email);

        return hotelEntity.getId();
    }
  
    @Override
    public void del(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public void modify(Long id, Long hotelId) {
        MembersEntity membersEntity =
                membersRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        HotelEntity hotelEntity =
                hotelRepository.findById(hotelId).orElseThrow(EntityNotFoundException::new);

        hotelEntity.setMembersEntity(membersEntity);
        membersEntity.setHotelEntity(hotelEntity);

        hotelRepository.save(hotelEntity);

    }
}
