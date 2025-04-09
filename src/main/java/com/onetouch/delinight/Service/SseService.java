package com.onetouch.delinight.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class SseService {


    // 맵타입으로 스트링(스토어 아이디랑, 유저 이메일), 이미터로 JVM 힙스택에 서버 시작하면 로그인할때 컨트롤러에서 커넥트 엔드포인트 실행 하면 밀어넣고
    // 푸쉬하는 쪽 메소드에 이미터s에서 해당 이미터가 존재하면 거기에 데이터 보내는 로직을 구현 하기
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final StoreService storeService;

    public SseEmitter connectUser(String email){
        SseEmitter emitter = new SseEmitter(60*60*1000L); // 1시간 유효하게
            emitters.put(email, emitter);



        return null;
    }
}
