package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Repository.NetPromoterScoreRepository;
import com.onetouch.delinight.Util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
@EnableAsync
public class NetPromoterScoreServiceImpl implements NetPromoterScoreService {

    private final NetPromoterScoreRepository netPromoterScoreRepository;
    private final CheckOutLogRepository checkOutLogRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;


    @Override
    public String sendNpsTemporary(Long checkOutId) {
        log.info("들어온 체크아웃 ID = {}" ,checkOutId);
        // 1. 사용자 검증
        CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);
        log.info("실핸한 쿼리 = {}", checkOut);

        String email = checkOut.getUsersEntity().getEmail();
        String name = checkOut.getUsersEntity().getName();

        String surveyLink = "http://localhost:8080/users/nps/survey/" + checkOutId;

        log.info("email = {}| name = {} | surveyLink = {}", email, name, surveyLink);
        Map<String, Object> variables = Map.of(
                "email", checkOut.getUsersEntity().getEmail(),
                "name", checkOut.getUsersEntity().getName(),
                "surveyLink", surveyLink
        );

        emailService.sendNpsEmail(
                email,
                name,
                surveyLink,
                checkOutId
        );

        return "NPS 설문 이메일이 전송되었습니다.";
    }

    @Override
    public NetPromoterScoreDTO npsInsert(Long checkOutId) {

        CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);

        NetPromoterScoreEntity nps = new NetPromoterScoreEntity();

        checkOut.setNetPromoterScoreEntity(nps);
        nps.setCheckOutLogEntity(List.of(checkOut));


        return null;
    }


    @Override
    public List<NetPromoterScoreDTO> npsList() {
        List<NetPromoterScoreEntity> netPromoterScoreEntitiesList = netPromoterScoreRepository.findAll();
        List<NetPromoterScoreDTO> netPromoterScoreDTOList = netPromoterScoreEntitiesList.stream()
                .map(entity -> modelMapper.map(entity, NetPromoterScoreDTO.class)).toList();
        return netPromoterScoreDTOList;
    }

    @Override
    public NetPromoterScoreDTO npsDetail(Long npsId) {
        NetPromoterScoreEntity netPromoterScoreEntity = netPromoterScoreRepository.findById(npsId).orElseThrow(EntityNotFoundException::new);
        NetPromoterScoreDTO netPromoterScoreDTO = modelMapper.map(netPromoterScoreEntity, NetPromoterScoreDTO.class);
        return netPromoterScoreDTO;
    }


}
