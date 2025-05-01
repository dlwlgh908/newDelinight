package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.NpsFormDataDTO;

import java.util.List;

public interface NetPromoterScoreService {

    public void sendNpsTemporary(Long checkOutId);

    public NetPromoterScoreDTO npsSelect(Long checkOutId);

    public NpsFormDataDTO npsInsert(Long checkOutId);

    public List<NetPromoterScoreDTO> npsList();

    public NetPromoterScoreDTO npsDetail(Long npsId);

}
