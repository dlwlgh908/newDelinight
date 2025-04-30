package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;

    @GetMapping("/survey/{checkOutId}")
    public String surveyForm(@PathVariable("checkOutId") Long checkOutId, Model model) {

        log.info(checkOutId);
        // 체크아웃된 사용자 정보 가져오기
        NetPromoterScoreDTO npsDTO = new NetPromoterScoreDTO();
        npsDTO.setId(checkOutId);

        model.addAttribute("nps", npsDTO);

        // 호텔만 이용한 경우 || 호텔과 스토어 둘 다 이용한 경우
        if (npsDTO.isHotel()){
            return "/users/nps/survey";
        }else if (npsDTO.isHotelAndStore()){
            return "/users/nps/survey";
        }
        return "redirect:/";

    }

    @PostMapping("/survey")
    public String survey(@ModelAttribute NetPromoterScoreDTO npsDTO) {
        netPromoterScoreService.npsInsert(npsDTO);
        return "redirect:/users/nps/survey/" + npsDTO.getId();
    }





}
