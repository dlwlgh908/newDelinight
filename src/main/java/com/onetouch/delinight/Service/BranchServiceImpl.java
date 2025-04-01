/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService{
    private final MembersRepository membersRepository;
    private final ModelMapper modelMapper;


    @Override
    public void create(MembersDTO membersDTO) {
        MembersEntity membersEntity =
            modelMapper.map(membersDTO, MembersEntity.class);

            membersRepository.save(membersEntity);
    }

}
