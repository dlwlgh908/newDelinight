/*********************************************************************
 * 클래스명 : UsersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;

public interface UsersService {

    public void singUpUser(UsersDTO usersDTO);

    public boolean passwordChange(UsersDTO usersDTO);

    public String sendTemporaryPassword(UsersDTO usersDTO);

    public Integer urlCheck(String email);



}
