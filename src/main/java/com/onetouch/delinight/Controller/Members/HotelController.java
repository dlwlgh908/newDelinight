/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
    private final BrandService brandService;

    @GetMapping("/create")
    public String createView() {


        return "members/hotel/create";
    }


    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createRest(HotelDTO hotelDTO, Principal principal) {
        log.info(hotelDTO);
        hotelService.create(hotelDTO);

        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping("/list")
    public String listView(Model model, Principal principal) {
        MembersDTO membersDTO = membersService.findByEmail(principal.getName());
        List<HotelDTO> hotelDTOList =
                hotelService.list(membersDTO);

        List<BranchDTO> branchList = branchService.list(principal.getName());
        List<BrandDTO> brandList = brandService.list();

        model.addAttribute("hotelDTOList", hotelDTOList);
        model.addAttribute("branchList", branchList);
        model.addAttribute("brandList", brandList);
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
public String readView( Model model, @PathVariable("id") Long id) {



    HotelDTO hotelDTO =
        hotelService.findHotelDTOById(id);


    BrandDTO brandDTO =
        brandService.read(hotelDTO.getBrandId());

    hotelDTO.setBrandDTO(brandDTO);


    String imgUrl = imageService.readHotel(hotelDTO.getId());
    model.addAttribute("imgUrl", imgUrl);
    model.addAttribute("hotel", hotelDTO);

    return "members/hotel/read";
}
    
    @GetMapping("/update2/{id}")
    public String updateView2(Model model, @PathVariable("id") Long id) {

        log.info(id);

        HotelDTO hotelDTO =
                hotelService.findHotelDTOById(id);


        BrandDTO brandDTO =
                brandService.read(hotelDTO.getBrandId());

        List<BrandDTO> brandList = brandService.list();

        hotelDTO.setBrandDTO(brandDTO);

        String imgUrl = imageService.readHotel(hotelDTO.getId());
        model.addAttribute("imgUrl", imgUrl);
        model.addAttribute("hotel", hotelDTO);
        model.addAttribute("brandList", brandList);

        return "members/hotel/update2";
    }

//    @GetMapping("/update")
//    public String updateView(Principal principal, Model model, @PathVariable("id") Long id) {
//
//        log.info(id);
//
//        HotelEntity hotelEntity =
//                hotelRepository.findById(id).get();
//
//        model.addAttribute("hotel", hotelEntity);
//        String imgUrl = imageService.readHotel(hotelEntity.getId());
//        model.addAttribute("imgUrl", imgUrl);
//        return "members/hotel/update";
//    }

    @PostMapping("/update")
    public String updateProc(HotelDTO hotelDTO) {

        log.info(hotelDTO);


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
