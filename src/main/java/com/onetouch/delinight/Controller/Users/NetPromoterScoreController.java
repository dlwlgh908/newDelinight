package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import com.onetouch.delinight.Util.NpsSurveyScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/nps/")
@Log4j2
public class NetPromoterScoreController {

    private final NetPromoterScoreService netPromoterScoreService;
    private final NpsSurveyScheduler npsSurveyScheduler;

    @GetMapping("/npsmail/{checkOutId}")
    public String sendNpsTestMail(@PathVariable Long checkOutId, Model model) {

        log.info("NPS 메일 발송 테스트 시작");
        log.info("NPS 설문 발송 완료, checkOutId : {}" + checkOutId);

        // NPS 설문 발송 서비스 호출
        try {

            // NPS 설문 이메일 발송
            npsSurveyScheduler.sendNpsSurvey();
            model.addAttribute("message", "NPS 설문 이메일이 실제 고객에게 발송되었습니다.");

        } catch (Exception e) {

            log.error("NPS 설문 발송 중 오류 발생", e);
            model.addAttribute("message", "NPS 설문 이메일 발송에 실패했습니다. 관리자에게 문의해주세요.");

        }
        // 설문 페이지로 이동
        return "users/nps/survey";

    }

    @GetMapping("/survey/{checkOutId}")
    public String survey(@PathVariable Long checkOutId, Model model) {

        log.info("NPS 설문 데이터 조회, checkOutId : {} " + checkOutId);

        try {

            // 체크아웃 ID로 해당하는 주문 정보(OrdersDTO) 리스트 조회
            List<OrdersDTO> ordersDTOList = netPromoterScoreService.npsSelect(checkOutId);

            // 주문 리스트에서 StoreDTO만 추출하여 새로운 리스트 구성
            List<StoreDTO> storeDTOList = new ArrayList<>();
            ordersDTOList.forEach(ordersDTO -> {
                storeDTOList.add(ordersDTO.getStoreDTO());
            });

            // 첫 번째 주문에서 호텔 ID를 추출 (모든 주문이 동일 호텔에 속한다고 가정)
            Long hotelId = ordersDTOList.getFirst().getHotelId();
            String hotelName = ordersDTOList.getFirst().getHotelName();


            model.addAttribute("checkOutId", checkOutId);      // 체크아웃 ID
            model.addAttribute("storeDTOList", storeDTOList);  // 스토어 리스트 전달
            model.addAttribute("hotelId", hotelId);            // 호텔 이름 전달
            model.addAttribute("hotelName", hotelName);        // 호텔 이름 전달

            log.info(hotelId);

            return "/users/nps/survey";

        }catch (Exception e) {

            log.info("NPS 설문 데이터 조회 실패", e);
            return "/redirect:/users/nps/survey";

        }
    }












}
