/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.QnaRepository;
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

public class QnaServiceImpl implements QnaService {


    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    private final CheckInRepository checkInRepository;
    private final HotelRepository hotelRepository;
    private final UsersRepository usersRepository;

    @Override
    public QnaDTO register(QnaDTO qnaDTO, Long id) {
        QnaEntity qnaEntity = modelMapper.map(qnaDTO, QnaEntity.class);
        //체크인 id을 찾아와서
        CheckInEntity checkInEntity = checkInRepository.findByRoomEntity_Id(id);
        qnaEntity.setCheckInEntity(checkInEntity);
        qnaEntity = qnaRepository.save(qnaEntity);
        qnaDTO = modelMapper.map(qnaEntity, QnaDTO.class);
        return qnaDTO;

    }

    @Override
    public QnaDTO register(QnaDTO qnaDTO, Long checkInId, Long usersId) {
        // 1. 체크인 정보 가져오기
        CheckInEntity checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new IllegalArgumentException("체크인 정보가 없습니다."));

        // 2. 유저 정보 가져오기
        UsersEntity usersEntity = usersRepository.findById(usersId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));

        // 3. 호텔 정보는 체크인 안에 있음
        HotelEntity hotelEntity = checkIn.getRoomEntity().getHotelEntity();

        // 4. QnaEntity로 변환 후 저장
        QnaEntity qnaEntity = QnaEntity.builder()
                .title(qnaDTO.getTitle())
                .content(qnaDTO.getContent())
                .checkInEntity(checkIn)
                .usersEntity(usersEntity)
                .hotelEntity(hotelEntity)
                .build();
        qnaRepository.save(qnaEntity).getId();

        return null;
    }

    @Override
    public Page<QnaDTO> qnaList(Pageable pageable,String email) {
        //이메일로 사장님의 호텔을 찾고 그 호텔에 등록된 Qna들을 페이지 단위로 가져와서
        //QnaDTO로 변환해서 리턴

        HotelEntity hotelEntity = hotelRepository.findByMembersEntity_Email(email);
        //이메일로 MembersEntity을 찾고, 그 회원이 소속된 호텔을 가져온다
        //이메일을 가진 사장님의 호텔 정보를 찾는다

        if (hotelEntity == null){//hotelEntity가 null일 수 있으므로 체크
            // null이면 Qna가 없다는 뜻이니 빈 페이지 반환 (에러 안 나게!)
            return Page.empty();
        }
        Page<QnaEntity> pageList = qnaRepository.findByHotelEntity_Id(hotelEntity.getId(),pageable);
        //찾은 호텔 id로 해당 호텔에 대한 Qna엔티티 목록을 페이징 처리해서 가져옴
        // 이 호텔에 등록된 Qna들을 페이지로 가져와

        return pageList.map(data -> modelMapper.map(data, QnaDTO.class));
        //가져온 Qna들을 QnaDTO로 변환해서 리턴
        //엔티티를 dto로 바꿔주는 역할

    }

    @Override
    public List<QnaDTO> qnaList(Long hotelId) {
        //특정 호텔에 등록된 QnA들을 DB에서 가져오고 그걸 DTO로 변환해서

        List<QnaEntity> qnaEntityList = qnaRepository.findByHotelEntity_Id(hotelId);
        //DB에 저장된 Qna정보를 가져오고 hotelId에 해당하는 Qna들만 골라서 가져와

        List<QnaDTO> qnaDTOList = qnaEntityList.stream().map(data -> modelMapper.map(data, QnaDTO.class)
                .sethotelDTO(modelMapper.map(data.getHotelEntity(),HotelDTO.class))).toList();
        //QnA Entity 하나하나를 돌면서 (.stream().map(...)), modelMapper라는 도구를 써서 QnaDTO로 바꿔줘.
        //data.getHotelEntity()를 HotelDTO로 바꿔서 .sethotelDTO(...)로 넣어주는 거야.
        //이걸 리스트로 만들어서 qnaDTOList에 저장하는 거지.

        return qnaDTOList;
    }


    @Override
    public QnaDTO read(Long id) {
        Optional<QnaEntity>optionalQnaEntity = qnaRepository.findById(id);
        QnaEntity qnaEntity = optionalQnaEntity.get();
        QnaDTO qnaDTO = modelMapper.map(qnaEntity, QnaDTO.class);
        return qnaDTO;
    }

    @Override
    public QnaDTO update(QnaDTO qnaDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaDTO.getId());
        QnaEntity qnaEntity = optionalQnaEntity.get();
        qnaEntity.setTitle(qnaDTO.getTitle());
        qnaEntity.setContent(qnaDTO.getContent());
        qnaRepository.save(qnaEntity);
        return null;
    }

    @Override
    public void delete(Long id) {
        qnaRepository.deleteById(id);

    }

}
