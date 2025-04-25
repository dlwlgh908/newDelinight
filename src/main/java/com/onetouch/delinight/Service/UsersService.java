/*********************************************************************
 * 클래스명 : UsersService
 * 기능 : 회원가입 , 비밀번호 변경 & 찾기 , 정보수정, 회원탈퇴
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.UsersEntity;

public interface UsersService {

    public void singUpUser(UsersDTO usersDTO);

    public boolean passwordChange(UsersDTO usersDTO);

    public String sendTemporaryPassword(UsersDTO usersDTO);

    public void userUpdate(String email , UsersDTO usersDTO);

    public void userDelete(String email);

    public UsersDTO findUsersByEmail(String email);



}
