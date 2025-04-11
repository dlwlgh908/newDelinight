package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.QnaDTO;
import com.onetouch.delinight.DTO.ReplyDTO;
import com.onetouch.delinight.Entity.QnaEntity;
import com.onetouch.delinight.Entity.ReplyEntity;
import com.onetouch.delinight.Repository.QnaRepository;
import com.onetouch.delinight.Repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReplyDTO register(ReplyDTO replyDTO) {

        Optional<QnaEntity> optionalQnaEntity =
                qnaRepository.findById(replyDTO.getId());

        QnaEntity qnaEntity =
                optionalQnaEntity.orElseThrow(EntityNotFoundException::new);

        log.info("서비스에 들어온 dto : " + replyDTO);

        ReplyEntity replyEntity =
                modelMapper.map(replyDTO,ReplyEntity.class);

        replyEntity.setQnaEntity(qnaEntity);

        ReplyEntity result = replyRepository.save(replyEntity);

        log.info("저장후 :" + result);

        replyDTO =
        modelMapper.map(result,ReplyDTO.class);

        replyDTO.setQnaDTO(modelMapper.map(result.getQnaEntity(), QnaDTO.class));

        log.info("저장 후 데이터 변환 DTO" +replyDTO);

        return replyDTO;
    }

    public ReplyEntity registerA(ReplyDTO replyDTO) {
        ReplyEntity replyEntity =
            modelMapper.map(replyDTO, ReplyEntity.class);
        log.info(replyEntity);
        return replyRepository.save(replyEntity);
    }

    @Override
    public List<ReplyDTO> findAll() {
        return List.of();
    }

    @Override
    public ReplyDTO update(ReplyDTO replyDTO) {
        return null;
    }
}
