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
    public void npsInsert(NetPromoterScoreDTO netPromoterScoreDTO) {
        NetPromoterScoreEntity netPromoterScoreEntity = modelMapper.map(netPromoterScoreDTO, NetPromoterScoreEntity.class);
        netPromoterScoreRepository.save(netPromoterScoreEntity);
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
