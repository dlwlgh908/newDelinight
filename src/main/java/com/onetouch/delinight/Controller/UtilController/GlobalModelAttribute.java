package com.onetouch.delinight.Controller.UtilController;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Service.HotelService;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.StoreService;
import com.onetouch.delinight.Util.CustomGuestDetails;
import com.onetouch.delinight.Util.CustomUserDetails;
import com.onetouch.delinight.Util.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.naming.Name;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalModelAttribute {

    private final StoreService storeService;
    private final MembersService membersService;
    private final HotelService hotelService;

    @ModelAttribute
    public void setGlobalAttributes(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        if (uri.startsWith("/members")) {

            if (principal instanceof MemberDetails memberDetails) {
                try {

                    if (memberDetails.getMembersEntity().getRole() == Role.STOREADMIN) {
                    log.info(storeService.findStoreByEmail(memberDetails.getUsername()));
                    log.info(memberDetails);
                    Integer alertCount = storeService.awaitingCountCheck(storeService.findStoreByEmail(memberDetails.getUsername()));
                    model.addAttribute("alertCount", alertCount);
                    // 스토어 어드민일때
                } else if (memberDetails.getMembersEntity().getRole() == Role.ADMIN) {
                    Integer alertCount = hotelService.unansweredCheck(hotelService.findHotelByEmail(memberDetails.getUsername()));
                    model.addAttribute("alertCount", alertCount);
                    // 호텔 어드민일때(수정 해야함)
                } else {
                    Integer alertCount = membersService.countOfRequestAccount(memberDetails.getUsername());
                    model.addAttribute("alertCount", 0L);
                    // 아무것도 아닐때(0으로 리턴)
                }
                model.addAttribute("member", memberDetails.getMembersEntity());
                model.addAttribute("role", memberDetails.getMembersEntity().getRole());
                }
                catch (Exception e){

                    throw new RuntimeException("멤버 로그인 필요");


                }
            }
        } else if (uri.startsWith("/users")) {
            try {
            if(uri.startsWith("/users/login")){}
            if(uri.startsWith("/users/logout")){}
            if(uri.startsWith("/users/welcome")){}
            else if (principal instanceof CustomUserDetails customUserDetails) {
                String name = customUserDetails.getUsersEntity().getName();
                model.addAttribute("data", name);
            }
            else if (principal instanceof CustomGuestDetails customGuestDetails){
                String phone = customGuestDetails.getUsername();
                model.addAttribute("data", phone);
            }
            }
            catch (Exception e){
                throw  new RuntimeException("게스트 로그인 필요");
            }
        }


    }
}
