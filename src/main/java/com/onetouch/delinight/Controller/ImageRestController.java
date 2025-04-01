/*********************************************************************
 * 클래스명 : MembersController
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.Controller;

import com.onetouch.delinight.Service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/img")
@Log4j2
public class ImageRestController {

    private final ImageService imageService;




    @PostMapping("/register")
    public ResponseEntity<Long> imgRegister(@RequestPart(name = "image") MultipartFile multipartFile){
        log.info("===================");
        log.info("들어온 이미지 파일");
        Long imgNum = imageService.register(multipartFile);
        return ResponseEntity.ok(imgNum);
    }

    @DeleteMapping("/delete/{imgNum}")
    public ResponseEntity<String> imgDelete(@PathVariable("imgNum")Long imgNum){
        log.info(imgNum);
        imageService.delete(imgNum);
        return ResponseEntity.ok("삭제를 성공하였습니다.");
    }

    @PostMapping("/update")
    public ResponseEntity<Long> imgUpdate(@RequestParam(name="image") MultipartFile multipartFile, @RequestParam(name = "imgNum") Long imgNum){

        return ResponseEntity.ok(imgNum);
    }

}
