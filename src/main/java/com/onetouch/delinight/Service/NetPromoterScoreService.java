package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.OrdersDTO;

import java.util.List;

public interface NetPromoterScoreService {

    public void sendNpsTemporary(Long checkOutId);

    public List<OrdersDTO> npsSelect(Long checkOutId);

    public void npsInsert(List<NetPromoterScoreDTO> npsDTOList, Long checkOutId);

    public List<NetPromoterScoreDTO> findAll(Long memberId);



}
