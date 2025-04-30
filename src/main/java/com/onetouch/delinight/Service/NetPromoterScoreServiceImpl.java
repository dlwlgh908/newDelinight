package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Entity.NetPromoterScoreEntity;
import com.onetouch.delinight.Repository.NetPromoterScoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class NetPromoterScoreServiceImpl implements NetPromoterScoreService {

    private final NetPromoterScoreRepository netPromoterScoreRepository;
    private final ModelMapper modelMapper;


    @Override
    public void npsInsert(NetPromoterScoreDTO npsDTO) {

        Long checkOutId = npsDTO.getId();
        List<NetPromoterScoreEntity> npsEntityList = netPromoterScoreRepository.findByCheckOutLogEntities_Id(checkOutId);

        if (npsEntityList.isEmpty()) {

            throw new EntityNotFoundException("해당 체크아웃 로그와 연결된 NPS가 존재하지 않습니다. ID: " + checkOutId);

        }

        NetPromoterScoreEntity npsEntity = npsEntityList.get(0);

        if (npsEntity.getHotelEntity() != null && npsEntity.getStoreEntity() == null) {
            log.info("호텔 설문만 등록");
            npsDTO.HotelSurveyTo(npsEntity);

        } else if (npsEntity.getHotelEntity() != null && npsEntity.getStoreEntity() != null) {

            log.info("호텔 및 스토어 설문 등록");
            npsDTO.HotelSurveyTo(npsEntity);
            npsDTO.StoreSurveyTo(npsEntity);
            npsDTO.totalScore(); // 총점 계산
            npsEntity.setTotalScore(npsDTO.getTotalScore());

        } else {

            throw new EntityNotFoundException("호텔과 스토어 모두 존재하지 않는 경우");

        }

        netPromoterScoreRepository.save(npsEntity);
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
