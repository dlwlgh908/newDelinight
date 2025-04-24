/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.StoreService;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/store")
public class StoreController {
    private final StoreService storeService;
    private final ImageService imageService;
    private final MembersService membersService;

    @GetMapping("/create")
    public String createView() {

        return "members/store/create";
    }

    @PostMapping("/create")
    public String createProc(StoreDTO storeDTO) {
        storeService.create(storeDTO);

        return "redirect:/members/store/list";
    }

    @GetMapping("/list")
    public String listView(Model model) {

        List<StoreDTO> storeDTOList =
            storeService.list();
        model.addAttribute("storeDTOList", storeDTOList);

        return "members/store/listA";
    }

    @GetMapping("/update")
    public String update(@AuthenticationPrincipal MemberDetails memberDetails, Model model){

        String email = memberDetails.getUsername();
        Long storeId = storeService.findStoreByEmail(email);
        StoreDTO storeDTO = storeService.read(storeId);
        log.info(storeDTO);
        String imgUrl = imageService.readStore(storeId);
        model.addAttribute("storeDTO", storeDTO);
        model.addAttribute("imgUrl", imgUrl);

        return "members/store/update";
    }

    @PostMapping("/update")
    public String updatePost(StoreDTO storeDTO){
        storeService.update(storeDTO);
        return "redirect:/members/store/list";
    }

    @GetMapping("/memberlist")
    public String memberlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             Principal principal) {
//        List<MembersEntity> membersEntityList =
//            membersRepository.selectHotelAd();


        Page<MembersEntity> membersEntityList =
                membersService.getListStore(page, principal.getName());

        List<StoreDTO> storeDTOList =
            storeService.storeList(principal.getName());

        log.info(membersEntityList);
        log.info(membersEntityList);

        log.info(storeDTOList);
        log.info(storeDTOList);



        model.addAttribute("membersEntityList", membersEntityList);
        model.addAttribute("storeDTOList", storeDTOList);


        return "members/store/memberlist";
    }

}
