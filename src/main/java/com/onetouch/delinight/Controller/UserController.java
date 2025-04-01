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


}
