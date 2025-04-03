package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersRestController {
    // 이거 내가 왜 만들었지?
    // 술이 문제야 문제 ㅅㅂ
    private final UsersService usersService;

    // 회원 유형을 숫자로 매핑하는 Map 생성, Map.of 불변 객체(수정할 수 없음)
    private final Map<Integer, String> userTypeMap = Map.of(
            0, "비회원",
            1, "회원"
    );

    @GetMapping("/checkUser")
    public ResponseEntity<String> checkUser(@RequestParam(value = "email" , required = false) String email) {
        // Email 기반으로 회원 유형을 확인
        Integer userType = usersService.urlCheck(email);
        // userType → Map에 존재하는지 확인
        return userTypeMap.containsKey(userType)
                // 존대하면 해당하는 문자열 응답 반환(회원 또는 비회원)
                ? ResponseEntity.ok(userTypeMap.get(userType))
                // 존재하지 않으면 500Error
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
    }

}
