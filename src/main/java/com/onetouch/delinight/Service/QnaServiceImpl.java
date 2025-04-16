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
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.QnaRepository;
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

import static com.onetouch.delinight.Entity.QQnaEntity.qnaEntity;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2

public class QnaServiceImpl implements QnaService {


    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    private final CheckInRepository checkInRepository;



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
    public Page<QnaDTO> list(Pageable pageable) {

        Page<QnaEntity> pageList = qnaRepository.findAll(pageable);
        return pageList.map(data -> modelMapper.map(data, QnaDTO.class));
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
        qnaEntity.setContent(qnaDTO.getTitle());
        return null;
    }

    @Override
    public void delete(Long id) {
        qnaRepository.deleteById(id);

    }

}
