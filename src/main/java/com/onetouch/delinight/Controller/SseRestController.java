package com.onetouch.delinight.Controller;


import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.SseService;
import com.onetouch.delinight.Util.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@Log4j2
public class SseRestController {

    private final SseService sseService;
    private final MembersService membersService;


    // 유저와 멤버가 테이블이 달라 어떻게 분기 할 까 하다가 레이아웃에 각자 다른 스크립트를 첨부해
    // 다른 엔드포인트로 sse 접속

    @GetMapping("/connectUser")
    public SseEmitter connectUser(Principal principal){
        return sseService.connect("hansin@a.a");
    }

    // 유저는 따로 롤이 없어 바로 sse에 connect 시킴
    // 어드민은 호텔과 S를 받아 인수를 늘리는게 아닌 SSE String 첫 인수를 알파벳하나 붙혀 구분함
    // SSE emttiers를 구분하지 않았음으로 접속해있는 유저와 어드민이 한 메모리에 있어 검색할때 전체 검색 해야하나
    // 현재 사용자가 별로 없으므로 문제가 생길거 같지는 않음
    // 허나 서비스 시 좀 더 효율적으로 하기 위해선 emitters 자체를 나누는것도 나쁘지 않아 보임(필요한 곳에서만 조회)

    @GetMapping("/connectAdmin")
    public SseEmitter connectAdmin(@AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("진입");
        String email = memberDetails.getUsername();
        Map<Role, Long> admin = membersService.findRoleByEmail(email);
        Map.Entry<Role, Long> entry = admin.entrySet().iterator().next();
        Role role = entry.getKey();
        Long id = entry.getValue();
        if (role.equals(Role.STOREADMIN)) {
            String newId = "S" + id;
            log.info("newId = "+newId);
            return sseService.connect(newId);

        } else if (role.equals(Role.ADMIN)) {
            String newId = "H" + id;
            return sseService.connect(newId);
        }
        else
            return null;
    }

    @PostMapping("/test")
    public void test(){
        sseService.sendToSAdmin("S4","new-order","주문");
    }
}
