package com.onetouch.delinight.Controller.Users;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.InquireRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Service.MembersService;
import com.onetouch.delinight.Service.InquireService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/users/inquire")
public class UsersInquireController {
    private final InquireService inquireService;
    private final InquireRepository inquireRepository;
    private final MembersService membersService;
    private final UsersRepository usersRepository;
    private final CheckInRepository checkInRepository;

    //등록get
    @Transactional
    @GetMapping("/register")
    public String register(InquireDTO inquireDTO, Long roomID, Long usersId){
        //연관 엔티티 가져오기 (체크인, 유저, 호텔)
        CheckInEntity checkInEntity = checkInRepository.findById(roomID)
                .orElseThrow(() -> new RuntimeException("체크인 정보를 찾을 수 없습니다."));

        UsersEntity usersEntity = usersRepository.findById(usersId)
                .orElseThrow(()-> new RuntimeException("사용자 정보 없음"));

        HotelEntity hotelEntity = checkInEntity.getRoomEntity().getHotelEntity(); // CheckIn → Hotel 관계 있을 경우

        // DTO → Entity 변환
        InquireEntity inquireEntity = InquireEntity.builder()
                .title(inquireDTO.getTitle())
                .content(inquireDTO.getContent())
                .checkInEntity(checkInEntity)
                .usersEntity(usersEntity)
                .hotelEntity(hotelEntity)
                .build();

        // 저장
        inquireRepository.save(inquireEntity);

        return "/users/inquire/register";
    }
    //등록post
    @PostMapping("/register")
    public String registerPost(InquireDTO inquireDTO, @RequestParam Long roomId, Principal principal){
        if (principal == null) {
            throw new RuntimeException("로그인한 사용자만 문의글을 등록할 수 있습니다.");
        }
        //로그인 사용자 Id 가져오기
        String loginId = principal.getName();
        UsersEntity usersEntity = usersRepository.findByName(loginId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        //Inquire등록 (roomId, userId 함께 전달)
        inquireService.register(inquireDTO, roomId, usersEntity.getId());
        return "redirect:/users/inquire/list";
    }
    //목록
    @GetMapping("/list")
    public String list(@PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,Principal principal,
                       Model model){
        if (principal == null) {
            // 로그인 안 된 경우 로그인 페이지로 보내기
            return "redirect:/users/login";
        }
        //로그인한 사장님의 이메일로 Inquire리스트 가져오기
        String email = principal.getName();
        Page<InquireDTO> inquireDTOList = inquireService.inquireList(pageable, principal.getName());
        if (inquireDTOList.getPageable().isPaged()){
            log.info("현재 페이지 번호 : {} ",inquireDTOList.getPageable().getPageNumber());
        }else {
            log.info("페이징 정보 없음");
        }

        if (inquireDTOList.isEmpty()){
            model.addAttribute("message", "등록된 Inquire가 없습니다.");
        }
        model.addAttribute("inquireDTOList",inquireDTOList);
        log.info(inquireDTOList.getContent());

        return "/users/inquire/list";
    }



    //상세보기
    @GetMapping("/read")
    public String read(@RequestParam Long id, Model model, Principal principal, RedirectAttributes redirectAttributes){
        InquireDTO inquireDTO = inquireService.read(id);
        model.addAttribute("inquireDTO", inquireDTO);
        return "/users/inquire/read";
    }
    //수정get
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id")Long id,Model model){
        log.info("수정" +id);
        InquireDTO inquireDTO = inquireService.read(id);
        model.addAttribute("inquireDTO", inquireDTO);
        return "/users/inquire/update";
    }
    //수정post
    @PostMapping("/update")
    public String updatePost(InquireDTO inquireDTO){
        log.info(inquireDTO);
        inquireService.update(inquireDTO);
        return "redirect:/users/inquire/read?id="+ inquireDTO.getId();
    }
    //삭제
    @PostMapping("/delete")
    public String deleteGET(Long id){

        log.info("히히아이디" + id);
        inquireService.delete(id);
        return "redirect:/users/inquire/list";
    }





}
