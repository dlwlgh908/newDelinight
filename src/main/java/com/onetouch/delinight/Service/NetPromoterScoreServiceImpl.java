package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.*;
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
import java.util.stream.Collectors;

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
    private final MembersRepository membersRepository;


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
            log.info("처리 중인 NPS DTO 데이터 = {}", netPromoterScoreDTO);
            // NetPromoterScoreEntity 빌더 패턴으로 생성
            NetPromoterScoreEntity netPromoterScoreEntity = NetPromoterScoreEntity.builder()
                    .checkOutLogEntity(checkOutLogRepository.findById(checkOutId).get())    // 체크아웃 ID
                    .questionOne(netPromoterScoreDTO.getQuestionOne())                      // 설문1
                    .questionTwo(netPromoterScoreDTO.getQuestionTwo())                      // 설문2
                    .questionThree(netPromoterScoreDTO.getQuestionThree())                  // 설문3
                    .questionFour(netPromoterScoreDTO.getQuestionFour())                    // 설문4
                    .questionFive(netPromoterScoreDTO.getQuestionFive())                    // 설문5
                    .etcQuestion(netPromoterScoreDTO.getEtcQuestion())                      // 기타 문의사항
                    .totalScore(netPromoterScoreDTO.getTotalScore())                        // 합계점수
                    .insertTime(LocalDateTime.now())                                        // 설문 응답 시간 기록
                    .build();


            // 설문 대상이 호텔인지 매장인지 구분하여 설정
            if(netPromoterScoreDTO.getHotelOrStore().equals("hotel")){
                log.info("호텔 ID 조회중 = {}", netPromoterScoreDTO.getHotelId());
                netPromoterScoreEntity.setHotelEntity(hotelRepository.findById(netPromoterScoreDTO.getHotelId()).get());
            } else {
                log.info("스토어 ID 조회중 = {}", netPromoterScoreDTO.getStoreId());
                netPromoterScoreEntity.setStoreEntity(storeRepository.findById(netPromoterScoreDTO.getStoreId()).get());
            }

            netPromoterScoreRepository.save(netPromoterScoreEntity);

        }
    }

    @Override
    public List<NetPromoterScoreDTO> findAll(Long memberId) {
        // 1. 멤버 정보 조회
        MembersEntity membersEntity = membersRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        log.info("조회 요청: memberId={}, role={}", memberId, membersEntity.getRole());

        List<NetPromoterScoreDTO> netPromoterScoreDTOList;

        // 2. 역할에 따른 데이터 조회
        if (membersEntity.getRole() == Role.SUPERADMIN) {
            // SUPERADMIN 권한: 모든 NPS 데이터 조회
            log.info("SUPERADMIN 권한 - 모든 NPS 데이터 조회 시작");
            netPromoterScoreDTOList = netPromoterScoreRepository.findAll().stream().map(nps -> {
                        NetPromoterScoreDTO dto = modelMapper.map(nps, NetPromoterScoreDTO.class);
                        dto.setCheckOutId(nps.getCheckOutLogEntity().getId());

                        // 호텔과 스토어 관련 정보 설정
                        if (nps.getHotelEntity() != null) {
                            dto.setHotelId(nps.getHotelEntity().getId());
                            dto.setHotelName(nps.getHotelEntity().getName());
                            dto.setHotelOrStore("hotel");
                        }
                        if (nps.getStoreEntity() != null) {
                            dto.setStoreId(nps.getStoreEntity().getId());
                            dto.setStoreName(nps.getStoreEntity().getName());
                            dto.setHotelOrStore("store");
                        }
                        dto.setInsertTime(nps.getInsertTime());
                        return dto;
                    })
                    .collect(Collectors.toList());
            log.info("SUPERADMIN 권한 - 총 {}건 조회됨", netPromoterScoreDTOList.size());
        } else if (membersEntity.getRole() == Role.ADMIN) {
            // ADMIN 권한: 호텔 ID에 따라 NPS 데이터 조회 (자기 호텔의 설문과 그에 속한 스토어들)
            log.info("ADMIN 권한 - 호텔 ID: {} 관련 NPS 데이터 조회 시작", membersEntity.getHotelEntity().getId());
            netPromoterScoreDTOList = netPromoterScoreRepository.findByHotelEntityOrStoreEntityIn(
                            membersEntity.getHotelEntity(), membersEntity.getHotelEntity().getStores() // 해당 호텔에 속한 모든 스토어
                    ).stream().map(nps -> {
                        NetPromoterScoreDTO dto = modelMapper.map(nps, NetPromoterScoreDTO.class);
                        dto.setCheckOutId(nps.getCheckOutLogEntity().getId());

                        // 호텔과 스토어 관련 정보 설정
                        if (nps.getHotelEntity() != null) {
                            dto.setHotelId(nps.getHotelEntity().getId());
                            dto.setHotelName(nps.getHotelEntity().getName());
                            dto.setHotelOrStore("hotel");
                        }
                        if (nps.getStoreEntity() != null) {
                            dto.setStoreId(nps.getStoreEntity().getId());
                            dto.setStoreName(nps.getStoreEntity().getName());
                            dto.setHotelOrStore("store");
                        }
                        dto.setInsertTime(nps.getInsertTime());
                        return dto;
                    })
                    .collect(Collectors.toList());
            log.info("ADMIN 권한 - 총 {}건 조회됨", netPromoterScoreDTOList.size());
        } else if (membersEntity.getRole() == Role.STOREADMIN) {
            Long storeId = membersEntity.getStoreEntity().getId();
            membersEntity.setStoreEntity(storeRepository.findById(storeId).get());

            log.info("STOREADMIN 권한 - 내 스토어 ID: {} 관련 NPS 데이터 조회 시작", storeId);

            if (membersEntity.getStoreEntity() == null) {
                log.error("STOREADMIN인데 storeEntity가 null입니다. memberId={}", memberId);
                throw new IllegalStateException("스토어 관리자에게 storeEntity가 존재하지 않습니다.");
            }

            netPromoterScoreDTOList = netPromoterScoreRepository.findByStoreEntity(membersEntity.getStoreEntity()).stream().map(nps -> {
                        NetPromoterScoreDTO dto = modelMapper.map(nps, NetPromoterScoreDTO.class);
                        dto.setCheckOutId(nps.getCheckOutLogEntity().getId());

                        if (nps.getHotelEntity() != null) {
                            dto.setHotelId(nps.getHotelEntity().getId());
                            dto.setHotelName(nps.getHotelEntity().getName());
                            dto.setHotelOrStore("hotel");
                        }
                        if (nps.getStoreEntity() != null) {
                            dto.setStoreId(nps.getStoreEntity().getId());
                            dto.setStoreName(nps.getStoreEntity().getName());
                            dto.setHotelOrStore("store");
                        }

                        dto.setInsertTime(nps.getInsertTime());
                        return dto;
                    })
                    .collect(Collectors.toList());
            log.info("STOREADMIN 권한 - 총 {}건 조회됨", netPromoterScoreDTOList.size());

        } else {
            log.info("알 수 없는 역할로 인한 예외 발생: memberId={}, role={}", memberId, membersEntity.getRole());
            throw new RuntimeException("알 수 없는 관리자 역할");
        }

        return netPromoterScoreDTOList;
    }




}
