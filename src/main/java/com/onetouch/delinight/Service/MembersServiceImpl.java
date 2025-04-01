/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{
    private final MembersRepository membersRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(MembersDTO membersDTO) {

        MembersEntity membersEntity =
            modelMapper.map(membersDTO, MembersEntity.class);
        membersEntity.setRole(Role.SUPERADMIN);
        membersEntity.setStatus(Status.WAIT);

            membersRepository.save(membersEntity);


    }

    @Override
    public List<MembersDTO> findAll() {
        List<MembersEntity> membersEntityList = membersRepository.findAll();

        List<MembersDTO> membersDTOList =
                membersEntityList.stream().toList().stream().map(
                        membersEntity -> modelMapper.map(membersEntity, MembersDTO.class)
                ).collect(Collectors.toList());

        return membersDTOList;
    }

    @Override
    public Page<MembersDTO> list(Pageable pageable) {
        int currentpage = pageable.getPageNumber() - 1;
        int limits = 10;
        Pageable temp = PageRequest.of(currentpage, limits, Sort.by(Sort.Direction.DESC, "id"));

        Page<MembersEntity> membersEntitie;
        membersEntitie = membersRepository.findAll(temp);

        Page<MembersDTO> membersDTOPage = membersEntitie.map(data -> modelMapper.map(data, MembersDTO.class));
        return membersDTOPage;
    }
}
