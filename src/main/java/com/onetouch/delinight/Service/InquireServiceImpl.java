/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.DTO.UsersDTO;
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
import java.util.Objects;
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
    private final CheckInService checkInService;
    private final SseService sseService;

    @Override
    public InquireDTO register(InquireDTO inquireDTO, String email) {
        InquireEntity inquireEntity = modelMapper.map(inquireDTO, InquireEntity.class);
        //체크인 id을 찾아와서

        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(email);
        CheckInEntity checkInEntity = checkInRepository.findById(checkInDTO.getId()).get();
        inquireEntity.setCheckInEntity(checkInEntity);


        inquireEntity = inquireRepository.save(inquireEntity);
        inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class);
        sseService.sendToHAdmin("H"+inquireEntity.getCheckInEntity().getRoomEntity().getHotelEntity().getId(),"new-inquire",inquireEntity.getCheckInEntity().getRoomEntity().getName()+"방으로 부터 새로운 문의가 들어왔습니다.");

        return inquireDTO;

    }

    @Override
    public Page<InquireDTO> inquireList(Pageable pageable,String email) {

        CheckInEntity checkInEntity = checkInRepository.findByUsersEntity_Email(email);
        //이메일로 체크인 정보를 찾는다
        if (checkInEntity == null){
            // null이면 Qna가 없다는 뜻이니 빈 페이지 반환 (에러 안 나게!)

            return Page.empty();
        }

        Page<InquireEntity> pageList = inquireRepository.findByCheckInEntity_Id(checkInEntity.getId(),pageable);
        //방금 찾은 체크인 기록의 id로 문의글을 찾고, 한페이지씩 잘라서 가져와

        return pageList.map(data -> modelMapper.map(data, InquireDTO.class));

    }




    @Override
    public List<InquireDTO> inquireList(Long hotelId) {


        List<InquireEntity> inquireEntityList = inquireRepository.findAllByHotelId(hotelId);


        List<InquireDTO> inquireDTOList = inquireEntityList.stream().map(data -> modelMapper.map(data, InquireDTO.class)
                .setHotelDTO(modelMapper.map(data.getHotelEntity(),HotelDTO.class))).toList();
        

        return inquireDTOList;
    }

    @Override
    public List<InquireDTO> inquireListByUsers(Long usersId) {

        //로그인한 유저의 Id로 문의글 엔티티 조회
        List<InquireEntity> inquireEntityList = inquireRepository.findByCheckInEntity_UsersEntity_Id(usersId);

        //엔티티를 DTO로 변환하고, 사용자 정보도 함께 매핑
        List<InquireDTO> inquireDTOList = inquireEntityList.stream().filter(Objects::isNull).map(inquire -> modelMapper.map(inquire, InquireDTO.class)
                .setUsersDTO(modelMapper.map(inquire.getUsersEntity(), UsersDTO.class))).toList();

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



    @Override
    public Page<InquireDTO> inquireListTEST(Pageable pageable,String email) {

        CheckInEntity checkInEntity = checkInRepository.findByUsersEntity_Email(email);
        //이메일로 체크인 정보를 찾는다
        if (checkInEntity == null){
            // null이면 Qna가 없다는 뜻이니 빈 페이지 반환 (에러 안 나게!)

            return Page.empty();
        }

        Page<InquireEntity> pageList = inquireRepository.findInquireEntitiesByCheckInEntity_Id(checkInEntity.getId(),pageable);
        //방금 찾은 체크인 기록의 id로 문의글을 찾고, 한페이지씩 잘라서 가져와

        return pageList.map(data -> modelMapper.map(data, InquireDTO.class));

    }


}
