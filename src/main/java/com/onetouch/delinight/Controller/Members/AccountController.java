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
import com.onetouch.delinight.Service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/account")
@Log4j2
public class AccountController {

    private final MembersService membersService;
    private final MembersRepository membersRepository;
    private final CenterService centerService;
    private final HotelService hotelService;
    private final PasswordEncoder passwordEncoder;
    private final OpenAIService openAIService;

    @GetMapping("/testAI")
    public String testAI(){
        String testPrompt = openAIService.analyzeSales("2025년 4월 1일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 2일\t순대국\t10000\t2\t20000\n" +
                "2025년 4월 2일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 2일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 2일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 3일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 3일\t수육\t14000\t1\t14000\n" +
                "2025년 4월 4일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 4일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 5일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 5일\t수육\t14000\t1\t14000\n" +
                "2025년 4월 6일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 6일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 7일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 7일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 8일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 8일\t수육\t14000\t1\t14000\n" +
                "2025년 4월 9일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 9일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 9일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 10일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 10일\t수육\t14000\t1\t14000\n" +
                "2025년 4월 11일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 11일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 12일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 12일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 12일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 13일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 13일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 14일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 14일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 15일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 15일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 15일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 16일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 17일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 17일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 18일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 19일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 20일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 21일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 22일\t수육\t14000\t1\t14000\n" +
                "2025년 4월 23일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 23일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 24일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 24일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 24일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 24일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 25일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 25일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 26일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 27일\t순대국\t10000\t1\t10000\n" +
                "2025년 4월 27일\t얼큰순대국\t11000\t1\t11000\n" +
                "2025년 4월 28일\t순대 한접시\t6000\t1\t6000\n" +
                "2025년 4월 28일\t돼지국밥\t8000\t1\t8000\n" +
                "2025년 4월 29일\t순대 한접시\t6000\t1\t6000 이게 이번달 매출이야 4월 29일 기준으로 요즘 매출에 대해 분석 좀 해 이 매출 트렌드를 가지고 이 가게 사장님 입장에서 수정 보완해야 할 부분 같은 솔루션좀  줘 추가적으로 너 답변은 스트링으로 받아서 메일에 집어 넣을꺼야 띄워쓰기 같은거 <br> 이런거 해주라 그리고 전일자랑 비교하고 이런 날짜에 따른 트렌드, 메뉴에 따른 트렌드 이런거 잘 분석해줘");
        log.info(testPrompt);

        return "members/account/common/redirectPage";

    }
    private final ModelMapper modelMapper;

    @GetMapping("/redirectPage")
    public String redirectPage() {

        return "members/account/common/redirectPage";
    }

    @GetMapping("/waitRedirectPage")
    public String waitRedirectPage() {

        return "members/account/common/waitRedirectPage";
    }

    @GetMapping("/accountRequest")
    public String accountRequest(String role, Long id, Model model) {

        if (role.equals("super")) {
            model.addAttribute("role", "본사 관리자");
            model.addAttribute("parentId", id);
        } else if (role.equals("hotel")) {
            model.addAttribute("role", "호텔 관리자");
            model.addAttribute("parentId", id);
        } else {
            model.addAttribute("role", "가맹점 관리자");
            model.addAttribute("parentId", id);
        }

        return "/members/account/common/requestForm";
    }

    @PostMapping("/accountRequest")
    public String accountRequestPost(MembersDTO membersDTO) {
        String roleStr = membersDTO.getRoleStr();

        if (roleStr.equals("super")) {
            membersDTO.setRole(Role.SUPERADMIN);
            membersService.create(membersDTO);
        } else if (roleStr.equals("hotel")) {
            membersDTO.setRole(Role.ADMIN);
            membersService.create(membersDTO);
        } else {
            membersDTO.setRole(Role.STOREADMIN);
            membersService.create(membersDTO);
        }



        return "redirect:/members/account/redirectPage";

    }

    @GetMapping("/dashHub")
    public String dashHub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);
        log.info("허브진입"+role);

        //로그인한 사람 롤 찾기
        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            if (membersService.assignCheck(principal.getName(), 1)) {
                return "redirect:/members/account/hotelAdmin/list";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        }
        //호텔 어드민
        else if (role.equals(Role.ADMIN)) {
            if (membersService.assignCheck(principal.getName(), 2)) {
                return "redirect:/members/account/storeAdmin/list";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        }
        //스토어 어드민

        else if (role.equals(Role.STOREADMIN)) {
            if (membersService.assignCheck(principal.getName(), 3)) {
                return "redirect:/members/store/orders/list";
            } else {
                return "redirect:/members/account/waitRedirectPage";
            }
        } else { // 시스템 어드민일 경우
            return "redirect:/members/account/superAdmin/list";

        }
    }

    @GetMapping("/accountHub")
    public String accountHub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);

        //시스템 어드민
        if (role.equals(Role.SYSTEMADMIN)) {
            return "redirect:/members/account/superAdmin/list";
        }
        //수퍼 어드민
        else if (role.equals(Role.SUPERADMIN)){
            return "redirect:/members/account/hotelAdmin/list";
        }
        //호텔 어드민
        else {
            return "redirect:/members/account/storeAdmin/list";
        }
    }

    @GetMapping("/centerhub")
    public String centerhub(Principal principal, Model model) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());
        model.addAttribute("role", role);

        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            return "redirect:/members/center/read";
        } else { // 시스템 어드민일 경우
            return "redirect:/members/center/list";
        }
    }

    @GetMapping("/paymenthub")
    public String paymenthub(Principal principal) {
        Role role = membersService.findOnlyRoleByEmail(principal.getName());

        //수퍼 어드민
        if (role.equals(Role.SUPERADMIN)) {
            return "redirect:/members/account/";
        }
        //호텔 어드민
        else if (role.equals(Role.ADMIN)) {
            return "redirect:/members/account/";
        }
        //스토어 어드민
        else if (role.equals(Role.STOREADMIN)) {
            return "redirect:/members/account/";
        } else { // 시스템 어드민일 경우
            return "redirect:/members/account/";
        }
    }

    @GetMapping("/hotelAdHome")
    public String home() {
        return "/members/account/common/hotelAdHome";
    }

    @GetMapping("/storeAdHome")
    public String storeAdHome() {
        return "/members/account/common/storeAdHome";
    }

    @GetMapping("/mypage")
    public String adminMyPage(Principal principal, Model model) {

        return "/members/account/common/mypage";
    }

    @GetMapping("/update")

    public String adminUpdate(Principal principal, Model model){
        MembersEntity members = membersRepository.findByEmail(principal.getName());
        model.addAttribute("member", members);
        return "/members/account/common/update";
    }

    @PostMapping("/update")
    public String updateProc(Principal principal,
                             @ModelAttribute MembersDTO membersDTO, Model model,
                             @RequestParam(value = "currentPassword", required = false) String currentPassword,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             @RequestParam(value = "newPhone", required = false) String newPhone,
                             @RequestParam(value = "confirmPassword", required = false) String confirmPassword) {


        MembersEntity membersEntity = membersRepository.findByEmail(principal.getName());
        boolean hasError = false;

        ////유효성검사
        ////전화번호
        //if (membersDTO.getPhone() == null || membersDTO.getPhone().trim().isEmpty()){
        //    model.addAttribute("phoneError", "전화번호를 입력해주세요.");
        //    hasError = true;
        //}
        ////비밀번호
        ////현재 비밀번호에 입력된 값이 있을 때 실행됨
        //if (currentPassword != null && !currentPassword.trim().isEmpty()){
        //
        //    //현재 비밀번호 검증
        //    if (!passwordEncoder.matches(currentPassword, membersEntity.getPassword())){
        //        model.addAttribute("currentPasswordError", "비밀번호가 일치하지 않습니다.");
        //        hasError = true;
        //    }
        //
        //    //새 비밀번호, 새 비밀번호 확인 검증
        //    if (!newPassword.equals(confirmPassword)){
        //        model.addAttribute("confirmPasswordError", "새 비밀번호 확인이 일치하지 않습니다.");
        //        hasError = true;
        //    }
        //}


        membersDTO =
            modelMapper.map(membersEntity, MembersDTO.class);

        membersService.update(membersDTO, newPhone ,newPassword);
        log.info(membersDTO);
        log.info(membersDTO);
        log.info(newPassword);
        log.info(newPassword);
        log.info(newPhone);
        log.info(newPhone);

        MembersEntity members = membersRepository.findByEmail(principal.getName());
        model.addAttribute("member", members);

        return "members/account/common/mypage";
    }


    @GetMapping("/superAdmin/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "status", required = false) String status, Principal principal) {
        log.info("list페이지 진입");

        Status status1;


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging = membersService.getListBystatus(status1, page);
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
                paging = membersService.findAccount(status1, page, principal.getName(), "hotel");
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

        Long parentId = hotelService.findHotelByEmail(principal.getName());


        Page<MembersEntity> paging = null;

        if (status != null && !status.isEmpty()) {
            if (status.equals("WAIT")) {
                status1 = Status.WAIT;
                paging = membersService.findAccount(status1, page, principal.getName(), "store");
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
    public String logout() {
        return "/members/account/common/logout";
    }

}