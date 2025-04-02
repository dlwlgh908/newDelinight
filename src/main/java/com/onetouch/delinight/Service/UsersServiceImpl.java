/*********************************************************************
 * 클래스명 : UsersServiceImpl
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-04-01
 * 수정 : 2025-04-01      기본 CRUD 설계
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.UsersRepository;
import com.onetouch.delinight.Util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService , UserDetailsService {

    private final UsersRepository usersRepository;


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
        log.info(".");

        usersRepository.save(usersEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsersEntity usersEntity = usersRepository.selectEmail(email);

        if (usersEntity == null) {
            log.info("findByEmail 찾을 수 없음");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return User.builder()
                .username(usersEntity.getEmail())
                .password(usersEntity.getPassword())
                .build();
    }


    // 비밀번호 찾기
    @Override
    public boolean passwordChange(UsersDTO usersDTO) {
        log.info("서비스로 들어온 비밀번호 찾기실행");

        if(!usersDTO.getPasswordOne().equals(usersDTO.getPasswordTwo())) {
            log.info("비번 틀림");
            return false;
        }

        UsersEntity usersEntity = usersRepository.selectEmail(usersDTO.getEmail());

        if (usersEntity == null) {
            log.info("서비스에서 유저를 찾아왔????" + usersEntity);
        }

        boolean matches = passwordEncoder.matches(usersDTO.getPasswordOne(), usersEntity.getPassword());
        log.info("DTO , Entity 비교 : " + matches);

        if (matches) {
            usersEntity.setPassword(passwordEncoder.encode(usersDTO.getPasswordOne()));
            return true;
        }else{
            log.info("기존 비밀번호와 다릅니다.");
            return false;
        }
    }


}
