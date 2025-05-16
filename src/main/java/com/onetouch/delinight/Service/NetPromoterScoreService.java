package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.OrdersDTO;

import java.util.List;

public interface NetPromoterScoreService {


    public List<OrdersDTO> npsSelect(Long checkOutId);

    public void npsInsert(List<NetPromoterScoreDTO> npsDTOList, Long checkOutId);

    public List<NetPromoterScoreDTO> npsAll(Long memberId);

    public List<Integer> dashboard(MembersDTO membersDTO);

    public String makePrompt(Long membersId);
    public List<Integer> dailyPerformanceScore(String email);
    public boolean npsOperationCheck(String email);



}
