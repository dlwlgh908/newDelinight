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
import java.util.stream.Collectors;

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
        //DB에 있는 모든 Qna 데이터를 가져오는 명령어
        List<ReplyEntity> replyEntityList = replyRepository.findAll();

        //리스트 하나씩 꺼내서 map 엔티티를 DTO로 변환해서 자동으로 필드 복사해주는
        List<ReplyDTO> replyDTOList =
                replyEntityList.stream().toList().stream().map(
                        replyEntity -> modelMapper.map(replyEntity, ReplyDTO.class)
                ).collect(Collectors.toList());//다시 리스트로 모아서 저장

        //DTO로 바꾼 Qna리스트를 반환
        return replyDTOList;
    }

    @Override
    public List<ReplyDTO> list(Long id) {
        List<ReplyEntity> replyEntityList =
                replyRepository.findByQnaEntity_Id(id);
        List<ReplyDTO> replyDTOList =
                replyEntityList.stream().map(
                        replyEntity -> {
                            ReplyDTO replyDTO =
                                    modelMapper.map(replyEntity, ReplyDTO.class);
                            return replyDTO;
                        }
                ).collect(Collectors.toList());

        log.info("값 : "+replyDTOList);
        return replyDTOList;
    }

    @Override
    public ReplyDTO read(Long id) {
        Optional<ReplyEntity> optionalReplyEntity =
                replyRepository.findById(id);
        ReplyEntity replyEntity = optionalReplyEntity.orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(replyEntity, ReplyDTO.class);
    }


    @Override
    public ReplyDTO update(ReplyDTO replyDTO) {
        Optional<ReplyEntity> optionalReplyEntity = replyRepository.findById(replyDTO.getId());
        ReplyEntity replyEntity = optionalReplyEntity.get();
        replyEntity.setReplyText(replyDTO.getReplyText());
        replyEntity.setReplyer(replyDTO.getReplyer());
        return null;
    }
}
