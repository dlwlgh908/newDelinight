/*********************************************************************
 * 클래스명 : UsersServiceImpl
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-04-01
 * 수정 : 2025-04-01      기본 CRUD 설계
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.GuestEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.GuestRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService , UserDetailsService {

    private final UsersRepository usersRepository;
    private final GuestRepository guestRepository;
    private final MembersRepository membersRepository;
    private final CheckInRepository checkInRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override // 회원가입
    public void singUpUser(UsersDTO usersDTO) {

        boolean exists = usersRepository.existsByEmail(usersDTO.getEmail());
        log.info("이거함????????????????????????????????????????????" + exists + usersDTO);

        if (exists) {
            throw new IllegalStateException("이미 가입된 이메일 입니다.");
        }

        UsersEntity usersEntity = modelMapper.map(usersDTO, UsersEntity.class);

        usersEntity.setPassword(passwordEncoder.encode(usersEntity.getPassword()));
        log.info("이메일 받고 인코딩한번더 받고 했뉘???????????????????????????????????");
        log.info(usersEntity.getEmail());
        log.info(usersEntity.getPassword());

        usersRepository.save(usersEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info(email);
        UsersEntity usersEntity = usersRepository.selectEmail(email);

        if (usersEntity == null) {


            MembersEntity membersEntity = membersRepository.findByEmail(email);


            if(membersEntity != null){
                return new MemberDetails(membersEntity);

            }
            GuestEntity guestEntity = checkInRepository.findByGuestEntity_Phone(email).getGuestEntity();

            if(guestEntity != null){
                return new CustomGuestDetails(guestEntity);
            }

            log.info("member, user 어디에서도 찾을 수 없음");
            throw new UsernameNotFoundException("member, user 어디에서도 찾을 수 없음");
        }

        log.info("생성 됐니?");
        return new CustomUserDetails(usersEntity);
    }


    // 비밀번호 변경
    @Override
    public boolean passwordChange(UsersDTO usersDTO) {
        log.info("서비스로 들어온 비밀번호 변경실행");

        if(!usersDTO.getPasswordOne().equals(usersDTO.getPasswordTwo())) {
            log.info("비번 틀림");
            return false;
        }


        UsersEntity usersEntity = usersRepository.selectEmail(usersDTO.getEmail());

        if (usersEntity == null) {
            log.info("서비스에서 유저를 찾아왔????" + usersEntity);
            return false;
        }

        // 기존 비밀번호 비교
        log.info("기존 비밀번호 (암호화된 상태): " + usersEntity.getPassword());
        log.info("입력한 비밀번호 (평문): " + usersDTO.getPasswordOne());

        boolean matches = passwordEncoder.matches(usersDTO.getPassword() , usersEntity.getPassword());
        log.info("DTO , Entity 비교 : " + matches);

        if (matches){
            // 기존 비밀번호와 일치하면 새 비밀번호로 업데이트
            String encodedPassword = PasswordUtil.encodePassword(usersDTO.getPasswordOne());
            usersEntity.setPassword(encodedPassword);
            // 변경된 비밀번호 Entity DB 저장
            usersRepository.save(usersEntity);
            log.info("술먹고 취해서 그런가 정말 왜 안되는지 모르겠어 ㅠㅠ");
            return true; // 성곡적으로 비밀번호 변경
        }else{
            return false;
        }

    }

    // 임시비밀번호 발급
    @Override
    @Transactional
    public String sendTemporaryPassword(UsersDTO usersDTO) {
        // 1. 회원 정보 검증
        UsersEntity usersEntity = usersRepository.selectEmail(usersDTO.getEmail());
        log.info(usersDTO);
        log.info(usersEntity);


        if (usersEntity == null) {
            throw new IllegalStateException("회원 정보를 찾을 수 없습니다.");
        }

        if ( !usersDTO.getName().equals(usersEntity.getName())) {
            throw new IllegalStateException("회원 정보를 찾을 수 없습니다.");
        }

        // 2. 임시 비번 생성
        String tempPassword = PasswordUtil.generateTempPassword();
        System.out.println("임시비번 : " + tempPassword);

        // 3. 비밀번호 암호화 후 저장
        usersEntity.setPassword(PasswordUtil.encodePassword(tempPassword));
        usersRepository.save(usersEntity);

        // 4. 이메일 발송을 위한 데이터 준비
        Map<String , Object> variables = Map.of(
                "name", usersEntity.getName(),
                "tempPassword", tempPassword
        );

        // 5. 이메일 전송(타임리프 텔플릿 사용)
        emailService.sendHtmlEmail(
                usersEntity.getEmail(),
                "임시 비밀번호 발급",
                "users/sendPasswordEmail", // 템플릿 이름(파일명: temp-password.html)
                tempPassword,
                variables
        );

        return "임시 비밀번호가 이메일로 발송되었습니다.";
    }

    @Override
    public void userDelete(String email) {
        try {
            usersRepository.deleteByEmail(email);
        }catch (Exception e){
            log.info("회원탈퇴 실패");
        }

    }

    @Override
    public void userUpdate(String email, UsersDTO usersDTO) {
        try {
            UsersEntity usersEntity = usersRepository.selectEmail(email);
            if (usersEntity == null) {
                log.info("해당 이메일을 가진 회원을 찾을 수 없음: " + email);
                throw new RuntimeException("해당 이메일을 가진 회원을 찾을 수 없습니다.");
            }
            log.info("기존 회원 정보: " + usersEntity);
            usersEntity.setPhone(usersDTO.getPhone());
            usersEntity.setAddress(usersDTO.getAddress());
            usersRepository.save(usersEntity);
            log.info("변경된 회원 정보 : " + usersEntity);
            log.info("회원정보 수정 성공: " + usersEntity);
        } catch (Exception e) {
            log.info("회원정보 수정 실패");
            throw new RuntimeException("회원정보 수정 중 오류 발생");
        }
    }

}

