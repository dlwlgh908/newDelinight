/*********************************************************************
 * 클래스명 : UsersController
 * 기능 : 회원가입 , 로그인 , 로그아웃 , 마이페이지 , 정보수정 , 비번변경 , 비번찾기
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

        private final UsersService usersService;


        @GetMapping("/home")
        public String usershome(Principal principal , Model model) {
                log.info("진입함?");

                model.addAttribute("data" , principal.getName());
                return "/users/home";
        }

        @GetMapping("/singUp")
        public String singUp() {
                return "/users/singUp";
        }

        @PostMapping("/singUp")
        public String singUpPost(@Valid UsersDTO usersDTO , BindingResult bindingResult) {

                if(bindingResult.hasErrors()) {
                        return "redirect:/users/singUp";
                }

                try {
                        usersService.singUpUser(usersDTO);
                }catch (Exception e) {
                        return "redirect:/users/singUp";
                }

                return "/users/login";
        }

        @GetMapping("/login")
        public String login() {
                return "/users/login";
        }


        // 비번 찾기
        @GetMapping("/passwordChange")
        public String passwordChangeGET() {
                log.info("passwordChange");
                return "/users/passwordChange";
        }

        @PostMapping("/passwordChange")
        public String passwordChangePost(UsersDTO usersDTO , RedirectAttributes redirectAttributes) {
                log.info("passwordChange_Post");
                log.info("변경된 값 : " +usersDTO);

                boolean result = usersService.passwordChange(usersDTO);
                log.info(result);

                if(result) {
                        return "redirect:/users/login";
                }else{
                        redirectAttributes.addFlashAttribute("msg" , "비밀번호를 변경할 수 없습니다.");
                        return "redirect:/users/passwordChange";
                }

        }


        // 임시 비번발급
        @GetMapping("/sendPassword")
        public String sendPasswordGET() {
                log.info("sendPassword진입완료!!!!!!!!!!!!!!");
                return "/users/sendPassword";
        }

        @PostMapping("/sendPassword")
        public String sendPasswordPost(UsersDTO usersDTO , Model model) {
                log.info("으아아아아아아아악 포스트진입함?????????");
                try {
                        // 임시 비밀번호 발급 서비스 호출
                        String resultMessage = usersService.sendTemporaryPassword(usersDTO);
                        model.addAttribute("message" , resultMessage); // 성공 메시지
                        log.info("으아아아아아아아악 성공함?????????");
                        return "/users/login";                                     // 성공 시 이동할 URL
                }catch (IllegalStateException e){
                        model.addAttribute("error" , e.getMessage());  // 오류 메시지
                        log.info("으아아아아아아아악 실패함?????????");
                        return "/users/login";                                     // 실패 시 이동할 URL
                }
        }







}
