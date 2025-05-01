package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;

import java.util.List;

public interface NetPromoterScoreService {

    public String sendNpsTemporary(Long checkOutId);

    public NetPromoterScoreDTO npsInsert(Long checkOutId);

    public List<NetPromoterScoreDTO> npsList();

    public NetPromoterScoreDTO npsDetail(Long npsId);

}
