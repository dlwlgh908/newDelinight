/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이효찬
 *********************************************************************/
package com.onetouch.delinight.Controller.Members;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Service.CenterService;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/account")
@Log4j2
public class AccountController {

    private final MembersService membersService;
    private final MembersRepository membersRepository;
    private final CenterService centerService;
    private final HotelService hotelService;

    @GetMapping("/redirectPage")
    public  String redirectPage(){

        return "members/account/common/redirectPage";
    }

    @GetMapping("/accountRequest")
    public String accountRequest(String role, Long id, Model model){

        if(role.equals("super")){
            model.addAttribute("role", "본사 관리자");
            model.addAttribute("parentId",id);
        }
        else if(role.equals("hotel")){
            model.addAttribute("role", "호텔 관리자");
            model.addAttribute("parentId", id);
        }
        else {
            model.addAttribute("role", "가맹점 관리자");
            model.addAttribute("parentId", id);
        }

        return "/members/account/common/requestForm";
    }

    @PostMapping("/accountRequest")
    public String accountRequestPost(MembersDTO membersDTO){
        String roleStr = membersDTO.getRoleStr();

        if(roleStr.equals("super")){
            membersDTO.setRole(Role.SUPERADMIN);
            membersService.create(membersDTO);
        }
        else if(roleStr.equals("hotel")){
            membersDTO.setRole(Role.ADMIN);
            membersService.create(membersDTO);
        }
        else {
            membersDTO.setRole(Role.STOREADMIN);
            membersService.create(membersDTO);
        }

        return "redirect:/members/account/redirectPage";

    }

    @GetMapping("/accounthub")
    public String accounthub(Principal principal){
        Role role = membersService.findOnlyRoleByEmail(principal.getName());

        //로그인한 사람 롤 찾기
        //수퍼 어드민
        if(role.equals(Role.SUPERADMIN)){
            return "redirect:/members/account/hotelAdmin/list";
        }
        //호텔 어드민
        else if(role.equals(Role.ADMIN)){
            return "redirect:/members/account/storeAdmin/list" ;
        }

        else{ // 시스템 어드민일 경우
            return "redirect:/members/account/superAdmin/list";
        }
    }

    @GetMapping("/centerhub")
    public String centerhub(Principal principal, Model model){
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);

        //수퍼 어드민
        if(role.equals(Role.SUPERADMIN)){
            return "redirect:/members/center/read";
        }
        else{ // 시스템 어드민일 경우
            return "redirect:/members/center/list";
        }
    }

    @GetMapping("/paymenthub")
    public String paymenthub(Principal principal){
        Role role = membersService.findOnlyRoleByEmail(principal.getName());

        //수퍼 어드민
        if(role.equals(Role.SUPERADMIN)){
            return "redirect:/members/account/";
        }
        //호텔 어드민
        else if(role.equals(Role.ADMIN)){
            return "redirect:/members/account/" ;
        }
        //스토어 어드민
        else  if(role.equals(Role.STOREADMIN)) {
            return "redirect:/members/account/";
        }
        else{ // 시스템 어드민일 경우
            return "redirect:/members/account/";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "/members/account/common/home";
    }

    @GetMapping("/mypage")
    public String adminMyPage(Principal principal, Model model){

        return "/members/account/common/mypage";
    }

    @GetMapping("/update")
    public String adminUpdate(Principal principal, Model model){

        return "/members/account/common/update";
    }


//    @GetMapping("/listB")
//    public String list(Model model) {
//        log.info("list페이지 진입");
//
//        List<MembersDTO> membersDTOList =
//                membersService.findSuper();
//
//        log.info(membersDTOList);
//        model.addAttribute("memberlist", membersDTOList);
//
//        return "members/listB";
//    }


    @GetMapping("/superAdmin/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "status", required = false) String status, Principal principal) {
        log.info("list페이지 진입");

        Status status1;


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging =membersService.getListBystatus(status1, page);
            } else if (status.equals("VALID")) {
                status1 = Status.VALID;
                paging = membersService.getListBystatus(status1, page);
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.getListBystatus(status1, page);
            }
        } else {
            paging = membersService.getList(page);

        }
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);

        return "members/account/superAdmin/list";
    }

    @GetMapping("/hotelAdmin/list")
    public String hoteladlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "status", required = false) String status, Principal principal) {
        log.info("list페이지 진입");

        Status status1;
        Long parentId = centerService.findCenter(principal.getName());



        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging =membersService.findAccount(status1, page, principal.getName(), "hotel");
            } else if (status.equals("VALID")) {

                status1 = Status.VALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
            }
        } else {
            paging = membersService.getListHotel(page, principal.getName());

        }
         model.addAttribute("parentId", parentId);
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);


        return "members/account/hotelAdmin/list";
    }

    @GetMapping("/storeAdmin/list")
    public String storeadlist(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "status", required = false) String status, Principal principal) {


        Status status1;

        Long parentId = hotelService.findHotelByEmail( principal.getName());


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging =membersService.findAccount(status1, page, principal.getName(), "store");
            } else if (status.equals("VALID")) {
                status1 = Status.VALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
            } else if (status.equals("NOTVALID")) {
                status1 = Status.NOTVALID;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
            }
        } else {
            paging = membersService.getListStore(page, principal.getName());

        }
        model.addAttribute("paging", paging);
        model.addAttribute("status", status);
        model.addAttribute("parentId", parentId);

        return "members/account/storeAdmin/list";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {

        if ("bad_credentials".equals(error)) {
            model.addAttribute("passwordError", "비밀번호가 틀립니다.");
        } else if ("unknown".equals(error)) {
            model.addAttribute("emailError", "이메일이 틀립니다.");
        }

        return "/members/account/common/login";
    }

    @GetMapping("/logout-success")
    public String logout(){
        return "/members/account/common/logout";
    }

}
