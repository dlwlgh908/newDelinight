/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Override
    public void register(MenuDTO menuDTO) {
        MenuEntity menuEntity = modelMapper.map(menuDTO, MenuEntity.class);
        menuEntity = menuRepository.save(menuEntity);
        menuDTO = modelMapper.map(menuEntity, MenuDTO.class);
    }

    @Override
    public MenuDTO read(Long id) {
        Optional<MenuEntity> optionalMenuEntity =
                menuRepository.findById(id);
        MenuEntity menuEntity = optionalMenuEntity.get();
        MenuDTO menuDTO = modelMapper.map(menuEntity, MenuDTO.class);
        return menuDTO;
    }

    @Override
    public Page<MenuDTO> menuList(Pageable pageable) {
        Page<MenuEntity> pageList = menuRepository.findAll(pageable);
        return pageList.map(data->modelMapper.map(data, MenuDTO.class));
    }


    @Override
    public MenuDTO update(MenuDTO menuDTO) {
        MenuEntity menuEntity =
                menuRepository.findById(menuDTO.getId()).orElseThrow(EntityNotFoundException::new);

        //메뉴명 수정
        menuEntity.setName(menuDTO.getName());
        //메뉴내용 수정
        menuEntity.setContent(menuDTO.getContent());
        //가격 수정
        menuEntity.setPrice(menuDTO.getPrice());
        //판매여부 수정
        menuEntity.setMenuStatus(menuDTO.getMenuStatus());

        return menuDTO;
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }




}
