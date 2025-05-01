package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Repository.*;
import com.onetouch.delinight.Util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final OrdersRepository ordersRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final StoreRepository storeRepository;


    @Override
    public void sendNpsTemporary(Long checkOutId) {
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

    }

    @Override
    public List<OrdersDTO> npsSelect(Long checkOutId) {
        log.info("npsSelect 들어온 ID = {}" ,checkOutId);
        CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);
        // 해당 체크아웃 ID에 속한 주문 목록 조회
        List<OrdersEntity> ordersEntityList = ordersRepository.findByCheckOutLogEntity_Id(checkOutId);
        // OrdersEntity → OrdersDTO 변환 및 필요한 정보(호텔명, 호텔ID, StoreDTO) 추가 매핑
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream()
                        .map(ordersEntity -> modelMapper.map(ordersEntity, OrdersDTO.class)
                        .setHotelName(checkOut.getRoomEntity().getHotelEntity().getName())                      // 호텔 이름
                        .setHotelId(checkOut.getRoomEntity().getHotelEntity().getId())                          // 호텔 ID
                        .setStoreDTO(modelMapper.map(ordersEntity.getStoreEntity(),StoreDTO.class))).toList();  // 매장
        return ordersDTOList;
    }

    @Override
    public void npsInsert(List<NetPromoterScoreDTO> npsDTOList, Long checkOutId) {

        for (NetPromoterScoreDTO netPromoterScoreDTO : npsDTOList) {

            // NetPromoterScoreEntity 빌더 패턴으로 생성
            NetPromoterScoreEntity netPromoterScoreEntity = NetPromoterScoreEntity.builder()
                    .checkOutLogEntity(checkOutLogRepository.findById(checkOutId).get())    // 체크아웃 ID
                    .QuestionOne(netPromoterScoreDTO.getQuestionOne())                      // 설문1
                    .QuestionTwo(netPromoterScoreDTO.getQuestionTwo())                      // 설문2
                    .QuestionThree(netPromoterScoreDTO.getQuestionThree())                  // 설문3
                    .QuestionFour(netPromoterScoreDTO.getQuestionFour())                    // 설문4
                    .QuestionFive(netPromoterScoreDTO.getQuestionFive())                    // 설문5
                    .insertTime(LocalDateTime.now())                                        // 설문 응답 시간 기록
                    .build();

            // 설문 대상이 호텔인지 매장인지 구분하여 설정
            if(netPromoterScoreDTO.getHotelOrStore().equals("hotel")){
                netPromoterScoreEntity.setHotelEntity(hotelRepository.findById(netPromoterScoreDTO.getHotelId()).get());
            } else {
                netPromoterScoreEntity.setStoreEntity(storeRepository.findById(netPromoterScoreDTO.getStoreId()).get());
            }

            netPromoterScoreRepository.save(netPromoterScoreEntity);

        }
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
