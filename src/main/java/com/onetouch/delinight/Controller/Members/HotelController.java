/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.BranchService;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.ImageService;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/hotel")
@Log4j2
public class HotelController {

    private final BranchService branchService;
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;
    private final MembersService membersService;
    private final ImageService imageService;

    @GetMapping("/create")
    public String createView() {


        return "members/hotel/create";
    }


    @PostMapping("/create")
    public String createProc(HotelDTO hotelDTO, String email, Principal principal) {
        email = principal.getName();
        hotelService.create(hotelDTO, email);

        return "members/hotel/create";
    }

    @GetMapping("/list")
    public String listView(Model model, Principal principal) {
        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        List<HotelDTO> hotelDTOList =
                hotelService.list(membersDTO);

        List<BranchDTO> branchList = branchService.list(principal.getName());

        model.addAttribute("hotelDTOList", hotelDTOList);
        model.addAttribute("branchList", branchList);
        return "members/hotel/listA";
    }

    @GetMapping("/read2")
    public String readView2(Principal principal, Model model) {


        log.info("principal log !!" + principal.getName());
        log.info("principal log !!" + principal.getName());
        log.info("principal log !!" + principal.getName());


        HotelEntity hotelEntity =
                hotelRepository.findByMembersEntity_Email(principal.getName());

        String imgUrl = imageService.readHotel(hotelEntity.getId());
        model.addAttribute("imgUrl", imgUrl);
        model.addAttribute("hotel", hotelEntity);

        return "members/hotel/read2";
    }
@GetMapping("/read/{id}")
public String readView(Principal principal, Model model, @PathVariable("id") Long id) {


    log.info("principal log !!" + principal.getName());
    log.info("principal log !!" + principal.getName());
    log.info("principal log !!" + principal.getName());
    log.info(id);
    log.info(id);
    log.info(id);
    log.info(id);


    MembersDTO membersDTO =
            membersService.findByEmail(principal.getName());
    log.info(membersDTO);
    log.info(membersDTO);

    HotelEntity hotelEntity =
            hotelRepository.findById(id).get();

    String imgUrl = imageService.readHotel(hotelEntity.getId());
    model.addAttribute("imgUrl", imgUrl);
    model.addAttribute("hotel", hotelEntity);

    return "members/hotel/read";
}
    
    @GetMapping("/update2/{id}")
    public String updateView2(Principal principal, Model model, @PathVariable("id") Long id) {

        log.info(id);

        HotelEntity hotelEntity =
                hotelRepository.findById(id).get();

        model.addAttribute("hotel", hotelEntity);
        String imgUrl = imageService.readHotel(hotelEntity.getId());
        model.addAttribute("imgUrl", imgUrl);
        return "members/hotel/update2";
    }

    @GetMapping("/update")
    public String updateView(Principal principal, Model model, @PathVariable("id") Long id) {

        log.info(id);

        HotelEntity hotelEntity =
                hotelRepository.findById(id).get();

        model.addAttribute("hotel", hotelEntity);
        String imgUrl = imageService.readHotel(hotelEntity.getId());
        model.addAttribute("imgUrl", imgUrl);
        return "members/hotel/update";
    }

    @PostMapping("/update")
    public String updateProc(@ModelAttribute HotelDTO hotelDTO) {

        log.info("update post 페이지" + hotelDTO);


        hotelService.update(hotelDTO);


        return "redirect:/members/hotel/read";
    }

    @GetMapping("/memberlist")
    public String memberlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             Principal principal) {
//        List<MembersEntity> membersEntityList =
//            membersRepository.selectHotelAd();


        Page<MembersEntity> membersEntityList =
            membersService.getListHotel(page, principal.getName());

        log.info(membersEntityList);
        List<HotelEntity> hotelEntityList =
            hotelRepository.selectallBySuper(principal.getName());
        log.info(hotelEntityList);

        model.addAttribute("membersEntityList", membersEntityList);
        model.addAttribute("hotelEntityList", hotelEntityList);


        return "members/hotel/memberlist";
    }

}
