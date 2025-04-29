package com.onetouch.delinight.Controller.UtilController;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.Service.RoomCareItemService;
import com.onetouch.delinight.Service.StoreService;
import com.onetouch.delinight.Util.CustomUserDetails;
import com.onetouch.delinight.Util.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalModelAttribute {

    private final StoreService storeService;
    private final RoomCareItemService roomCareItemService;


    @ModelAttribute
    public void setGlobalAttributes(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        if (uri.startsWith("/members")) {

            if (principal instanceof MemberDetails memberDetails) {
                try {

                    if (memberDetails.getMembersEntity().getRole() == Role.STOREADMIN) {


                        Long storeId = storeService.findStoreByEmail(memberDetails.getUsername());
                        if (storeId!=null) {
                            log.info("스토어 아이디: {}", storeId);

                            Integer alertCount = storeService.awaitingCountCheck(storeService.findStoreByEmail(memberDetails.getUsername()));
                            model.addAttribute("alertCount", alertCount);
                            model.addAttribute("storeId", storeId);
                        }
                        else {

                        }
                        // 스토어 어드민일때
                    } else if (memberDetails.getMembersEntity().getRole() == Role.ADMIN) {
                        Integer alertCount = 0;
                        model.addAttribute("alertCount", alertCount);
                        // 호텔 어드민일때(수정 해야함)
                    } else {
                        Integer alertCount = 0;
                        model.addAttribute("alertCount", alertCount);
                        // 아무것도 아닐때(0으로 리턴)
                    }
                    model.addAttribute("member", memberDetails.getMembersEntity());
                    model.addAttribute("role", memberDetails.getMembersEntity().getRole());

                    if (uri.startsWith("/members/roomCareItem")) {
                        List<RoomCareItemDTO> roomCareItemDTOList = roomCareItemService.list();
                        model.addAttribute("roomCareItemDTOList", roomCareItemDTOList);
                    }
                }
                catch (Exception e){

                    throw new RuntimeException("로그인 필요");


                }
            }
        } else if (uri.startsWith("/users")) {
            if(uri.startsWith("/users/login")){}
            if(uri.startsWith("/users/logout")){}
            if(uri.startsWith("/users/welcome")){}
            else if (principal instanceof CustomUserDetails customUserDetails) {
                String name = customUserDetails.getUsersEntity().getName();
                model.addAttribute("data", name);
                Integer alertCount = 0;//수정할 예정
                model.addAttribute("alertCount", alertCount);
            }
        }


    }
}
