package com.onetouch.delinight.Controller.Users;


import com.onetouch.delinight.DTO.NetPromoterScoreDTO;
import com.onetouch.delinight.DTO.NpsFormDataDTO;
import com.onetouch.delinight.Service.NetPromoterScoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/nps")
@Log4j2
public class NetPromoterScoreRestController {

    private final NetPromoterScoreService netPromoterScoreService;

    @GetMapping("/{checkOutId}")
    public ResponseEntity<?> NpsSelect(@PathVariable Long checkOutId){
        log.info("NPS 폼 API 요청 - checkOutId : {} " + checkOutId);
        try{
            NetPromoterScoreDTO npsDTO  = netPromoterScoreService.npsSelect(checkOutId);
            log.info("{}",npsDTO);
            return ResponseEntity.ok(npsDTO);
        }catch (EntityNotFoundException e){
            NpsFormDataDTO form = netPromoterScoreService.npsInsert(checkOutId);
            log.info("{}",form);
            return ResponseEntity.ok(form);
        }
    }

    @PostMapping("/{checkOutId}")
    public ResponseEntity<?> NpsForm(@PathVariable Long checkOutId, @RequestBody NpsFormDataDTO form) {
        netPromoterScoreService.npsInsert(checkOutId);
        return ResponseEntity.ok(form);
    }


}
