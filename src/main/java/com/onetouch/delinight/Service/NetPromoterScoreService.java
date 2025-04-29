package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;

import java.util.List;

public interface NetPromoterScoreService {

    public void npsInsert(NetPromoterScoreDTO netPromoterScoreDTO);

    public List<NetPromoterScoreDTO> npsList();

    public NetPromoterScoreDTO npsDetail(Long npsId);

}
