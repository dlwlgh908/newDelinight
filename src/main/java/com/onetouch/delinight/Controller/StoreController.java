/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/create")
    public String createView() {

        return "store/create";
    }

    @PostMapping("/create")
    public String createProc(StoreDTO storeDTO) {
        storeService.create(storeDTO);

        return "store/create";
    }

    @GetMapping("/listA")
    public String listView(Model model) {

        List<StoreDTO> storeDTOList =
            storeService.list();
        model.addAttribute("storeDTOList", storeDTOList);

        return "store/listA";
    }

}
