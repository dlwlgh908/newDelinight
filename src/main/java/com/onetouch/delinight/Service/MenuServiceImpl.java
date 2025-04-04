/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.MenuStatus;
import com.onetouch.delinight.DTO.MenuDTO;

import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.ImageRepository;
import com.onetouch.delinight.Repository.MenuRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final StoreRepository storeRepository;

    @Override
    public void register(MenuDTO menuDTO, String email) {

        log.info(menuDTO.getStatus());
        if(menuDTO.getStatus().equals("SELL")){
            menuDTO.setMenuStatus(MenuStatus.SELL);
        }
        else {
            menuDTO.setMenuStatus(MenuStatus.SOLD_OUT);
        }

        StoreEntity storeEntity = storeRepository.findByMembersEntity_Email(email);


        MenuEntity menuEntity = modelMapper.map(menuDTO, MenuEntity.class);
        menuEntity.setStoreEntity(storeEntity);
        menuEntity = menuRepository.save(menuEntity);

        if (menuDTO.getImgNum() != null) {
            Optional<ImageEntity> optionalImageEntity = imageRepository.findById(menuDTO.getImgNum());
            if (optionalImageEntity.isPresent()) {
                ImageEntity imageEntity = optionalImageEntity.get();
                imageEntity.setMenuEntity(menuEntity);
                imageRepository.save(imageEntity);
            }
        }

    }

    @Override
    public MenuDTO read(Long id) {
        Optional<MenuEntity> optionalMenuEntity =
                menuRepository.findById(id);
        MenuEntity menuEntity = optionalMenuEntity.get();
        MenuDTO menuDTO = modelMapper.map(menuEntity, MenuDTO.class);
        Optional<ImageEntity> imageEntity = imageRepository.findByMenuEntity_Id(id);
        if (imageEntity.isPresent()) {
            String imgUrl = imageEntity.get().getFullUrl();
            menuDTO.setImgUrl(imgUrl);
        }
        return menuDTO;
    }

    @Override
    public Page<MenuDTO> menuList(Pageable pageable) {
        Page<MenuEntity> pageList = menuRepository.findAll(pageable);
        return pageList.map(data -> modelMapper.map(data, MenuDTO.class));
    }


    @Override
    public MenuDTO update(MenuDTO menuDTO) {
        Optional<MenuEntity> optionalMenuEntity = menuRepository.findById(menuDTO.getId());
        MenuEntity menuEntity = optionalMenuEntity.get();
        menuEntity.setName(menuDTO.getName());
        menuEntity.setContent(menuDTO.getContent());
        menuEntity.setPrice(menuDTO.getPrice());
        menuEntity.setMenuStatus(menuDTO.getMenuStatus());
        ImageEntity imageEntity = imageRepository.findById(menuDTO.getImgNum()).get();

        if (imageRepository.findByMenuEntity_Id(menuDTO.getId()).isPresent()) {
            imageRepository.deleteByMenuEntity_Id(menuDTO.getId());
        }
        imageEntity.setMenuEntity(menuEntity);
        imageRepository.save(imageEntity);
        menuDTO = modelMapper.map(menuEntity, MenuDTO.class);


        return menuDTO;
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }


    @Override
    public List<MenuDTO> menuListByHotel(Long hotelNum) {
        List<MenuEntity> menuEntityList = menuRepository.findByStoreEntity_HotelEntity_Id(hotelNum);
        List<MenuDTO> menuDTOList = menuEntityList.stream().map(data -> modelMapper.map(data, MenuDTO.class)).toList();
        return menuDTOList;
    }
}
