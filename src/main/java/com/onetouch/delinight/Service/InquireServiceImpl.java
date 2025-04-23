/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Log4j2

public class InquireServiceImpl implements InquireService {


    private final InquireRepository inquireRepository;
    private final ModelMapper modelMapper;
    private final CheckInRepository checkInRepository;
    private final HotelRepository hotelRepository;
    private final UsersRepository usersRepository;

    @Override
    public InquireDTO register(InquireDTO inquireDTO, Long id) {
        InquireEntity inquireEntity = modelMapper.map(inquireDTO, InquireEntity.class);
        //체크인 id을 찾아와서
        CheckInEntity checkInEntity = checkInRepository.findByRoomEntity_Id(id);
        inquireEntity.setCheckInEntity(checkInEntity);
        inquireEntity = inquireRepository.save(inquireEntity);
        inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class);
        return inquireDTO;

    }

    @Override
    public Page<InquireDTO> inquireList(Pageable pageable,String email) {
        //이메일로 사장님의 호텔을 찾고 그 호텔에 등록된 Inquire들을 페이지 단위로 가져와서
        //InquireDTO로 변환해서 리턴

        HotelEntity hotelEntity = hotelRepository.findByMembersEntity_Email(email);
        //이메일로 MembersEntity을 찾고, 그 회원이 소속된 호텔을 가져온다
        //이메일을 가진 사장님의 호텔 정보를 찾는다

        if (hotelEntity == null){//hotelEntity가 null일 수 있으므로 체크
            // null이면 Inquire가 없다는 뜻이니 빈 페이지 반환 (에러 안 나게!)
            return Page.empty();
        }
        Page<InquireEntity> pageList = inquireRepository.findByHotelEntity_Id(hotelEntity.getId(),pageable);
        //찾은 호텔 id로 해당 호텔에 대한 Inquire엔티티 목록을 페이징 처리해서 가져옴
        // 이 호텔에 등록된 Inquire들을 페이지로 가져와

        return pageList.map(data -> modelMapper.map(data, InquireDTO.class));
        //가져온 Inquire들을 InquireDTO로 변환해서 리턴
        //엔티티를 dto로 바꿔주는 역할

    }

    @Override
    public List<InquireDTO> inquireList(Long hotelId) {
        //특정 호텔에 등록된 QnA들을 DB에서 가져오고 그걸 DTO로 변환해서

        List<InquireEntity> inquireEntityList = inquireRepository.findByHotelEntity_Id(hotelId);
        //DB에 저장된 Inquire정보를 가져오고 hotelId에 해당하는 Inquire들만 골라서 가져와

        List<InquireDTO> inquireDTOList = inquireEntityList.stream().map(data -> modelMapper.map(data, InquireDTO.class)
                .sethotelDTO(modelMapper.map(data.getHotelEntity(),HotelDTO.class))).toList();
        //QnA Entity 하나하나를 돌면서 (.stream().map(...)), modelMapper라는 도구를 써서 InquireDTO로 바꿔줘.
        //data.getHotelEntity()를 HotelDTO로 바꿔서 .sethotelDTO(...)로 넣어주는 거야.
        //이걸 리스트로 만들어서 inquireDTOList에 저장하는 거지.

        return inquireDTOList;
    }


    @Override
    public InquireDTO read(Long id) {
        Optional<InquireEntity>optionalInquireEntity = inquireRepository.findById(id);
        InquireEntity inquireEntity = optionalInquireEntity.get();
        InquireDTO inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class);
        return inquireDTO;
    }

    @Override
    public InquireDTO update(InquireDTO inquireDTO) {
        Optional<InquireEntity> optionalInquireEntity = inquireRepository.findById(inquireDTO.getId());
        InquireEntity inquireEntity = optionalInquireEntity.get();
        inquireEntity.setTitle(inquireDTO.getTitle());
        inquireEntity.setContent(inquireDTO.getContent());
        inquireRepository.save(inquireEntity);
        return null;
    }

    @Override
    public void delete(Long id) {
        inquireRepository.deleteById(id);

    }

}
