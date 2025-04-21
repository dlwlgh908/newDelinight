/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CenterDTO;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService{
    private final CenterRepository centerRepository;
    private final ModelMapper modelMapper;
    private final MembersRepository membersRepository;


    @Override
    public void create(CenterDTO centerDTO, String email) {

        MembersEntity membersEntity =
            membersRepository.findById(centerDTO.getMembersId()).get();

        CenterEntity centerEntity =
            modelMapper.map(centerDTO, CenterEntity.class);
        centerEntity.setMembersEntity(membersEntity);

        centerRepository.save(centerEntity);
    }

    @Override
    public List<CenterDTO> list() {
        List<CenterEntity> centerEntityList =
                centerRepository.findAll();
        List<CenterDTO> centerDTOList =
        centerEntityList.stream().toList().stream().map(
                centerEntity -> modelMapper.map(centerEntity, CenterDTO.class)
        ).collect(Collectors.toList());

        return centerDTOList;
    }

    @Override
    public MembersDTO checkEmail(String email) {
        MembersEntity membersEntity =
            membersRepository.findByEmail(email);
        MembersDTO membersDTO =
        modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public CenterDTO read(String email) {
        CenterEntity centerEntity =
            centerRepository.findByMembersEntity_Email(email);
        CenterDTO centerDTO =
            modelMapper.map(centerEntity, CenterDTO.class);
        return centerDTO;
    }
}
