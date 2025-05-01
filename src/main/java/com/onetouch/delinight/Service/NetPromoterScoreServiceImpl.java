package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.NpsFormDataDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CheckOutLogEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Repository.CheckOutLogRepository;
import com.onetouch.delinight.Repository.NetPromoterScoreRepository;
import com.onetouch.delinight.Repository.OrdersRepository;
import com.onetouch.delinight.Util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public NetPromoterScoreDTO npsSelect(Long checkOutId) {
        log.info("npsSelect 들어온 ID = {}" ,checkOutId);
        CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);

        NetPromoterScoreDTO npsDTO = new NetPromoterScoreDTO();
        HotelEntity hotel = checkOut.getRoomEntity().getHotelEntity();
        npsDTO.setHotelDTO(modelMapper.map(hotel, HotelDTO.class));

        List<OrdersEntity> order = ordersRepository.findByCheckOutLogEntity_Id(checkOutId);

        List<StoreDTO> store = order.stream().map(orderEntity -> modelMapper.map(orderEntity.getStoreEntity(), StoreDTO.class)).distinct().toList();
        npsDTO.setStoreDTOS(store);

        return npsDTO;
    }

    @Override
    public NpsFormDataDTO npsInsert(Long checkOutId) {

        log.info("npsInsert 들어온 ID = {}", checkOutId);
        CheckOutLogEntity checkOut = checkOutLogRepository.findById(checkOutId).orElseThrow(EntityNotFoundException::new);

        NetPromoterScoreEntity nps = checkOut.getNetPromoterScoreEntity();

        NpsFormDataDTO form = new NpsFormDataDTO();
        form.setCheckOutId(checkOutId);

        // 기존 설문 응답이 있다면 그 데이터를 DTO에 매핑
        if (nps != null) {

            List<Integer> hotelQuestions = List.of(
                    nps.getHotelQuestionOne(),
                    nps.getHotelQuestionTwo(),
                    nps.getHotelQuestionThree(),
                    nps.getHotelQuestionFour(),
                    nps.getHotelQuestionFive()
            );

            form.setHotelQuestions(hotelQuestions);

            List<Integer> storeQuestions = new ArrayList<>();

            // 스토어 설문 응답 처리 (스토어가 여러 개인 경우 각 5문항)
            if(nps.getStoreQuestionOne() != 0) storeQuestions.add(nps.getStoreQuestionOne());
            if(nps.getStoreQuestionTwo() != 0) storeQuestions.add(nps.getStoreQuestionTwo());
            if(nps.getStoreQuestionThree() != 0) storeQuestions.add(nps.getStoreQuestionThree());
            if(nps.getStoreQuestionFour() != 0) storeQuestions.add(nps.getStoreQuestionFour());
            if (nps.getStoreQuestionFive() != 0) storeQuestions.add(nps.getStoreQuestionFive());

            form.setStoreQuestions(storeQuestions);
            form.setEtcQuestion(nps.getEtcQuestion());
            form.setCompleted(nps.isCompleted());

        }else {

            // 새로운 설문이라면 빈 응답 세팅
            form.setHotelQuestions(new ArrayList<>(List.of(0, 0, 0, 0, 0)));
            form.setStoreQuestions(new ArrayList<>());
            form.setCompleted(false);

        }

        // 호텔 설문은 반드시 5문항 작성 해야함
        if (form.getHotelQuestions().size() != 5){
            throw new IllegalStateException("호텔 설문은 5문항을 반드시 작성해야 합니다.");
        }

        // 호텔 투숙 시에 주문을 통해 스토어를 이용했다면 각 스토어에 대해 설문을 작성할 수 있도록 조건 추가
        List<OrdersEntity> order = ordersRepository.findByCheckOutLogEntity_Id(checkOutId);
        if (!order.isEmpty()){
            List<Integer> storeQuestions = new ArrayList<>();
            for (OrdersEntity orders : order){
                storeQuestions.addAll(List.of(0, 0, 0, 0, 0));
            }
            form.setStoreQuestions(storeQuestions);
        }

        // 설문 데이터를 바로 저장
        if (nps == null) {
            nps = new NetPromoterScoreEntity();
        }

        // 호텔 설문 응답 저장
        List<Integer> hotelQuestions = form.getHotelQuestions();
        if (hotelQuestions.size() == 5) {
            nps.setHotelQuestionOne(hotelQuestions.get(0));
            nps.setHotelQuestionTwo(hotelQuestions.get(1));
            nps.setHotelQuestionThree(hotelQuestions.get(2));
            nps.setHotelQuestionFour(hotelQuestions.get(3));
            nps.setHotelQuestionFive(hotelQuestions.get(4));
        }

        // 스토어 설문 응답 저장
        List<Integer> storeQuestions = form.getStoreQuestions();
        if (storeQuestions != null && !storeQuestions.isEmpty()) {
            nps.setStoreQuestionOne(storeQuestions.get(0));
            nps.setStoreQuestionTwo(storeQuestions.get(1));
            nps.setStoreQuestionThree(storeQuestions.get(2));
            nps.setStoreQuestionFour(storeQuestions.get(3));
            nps.setStoreQuestionFive(storeQuestions.get(4));
        }

        // 기타 설문 응답 저장
        nps.setEtcQuestion(form.getEtcQuestion());

        // 설문 완료 여부 설정
        nps.setCompleted(true);

        // NPS 데이터 저장
        netPromoterScoreRepository.save(nps);

        return form;
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
