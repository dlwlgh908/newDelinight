package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.MenuRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class MenuServiceImplTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    StoreRepository storeRepository;





    @Test
    public void register() {

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("계란말이");
        menuDTO.setContent("계란은 단백질이니까");
        menuDTO.setPrice(4000);
        menuService.register(menuDTO);
    }

    @Test
    @Transactional
    public void store() {

        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("맥주");
        menuDTO.setContent("시원함");

        ModelMapper modelMapper = new ModelMapper();

        MenuEntity menuEntity =
                modelMapper.map(menuDTO, MenuEntity.class);
        StoreEntity storeEntity =
        storeRepository.findById(1L).get();
        menuEntity.setStoreEntity(storeEntity);

        log.info(menuEntity);
        log.info(menuEntity);
        log.info(menuEntity);
        menuRepository.save(menuEntity);


    }

    @Test
    public void read(){
        Long id = 1L;
        MenuDTO menuDTO = menuService.read(id);
        log.info(menuDTO);
    }
    @Test
    public void update(){
        MenuEntity menuEntity = menuRepository.findById(1L).get();
        menuEntity.setName("비빔밥");
        menuEntity.setContent("비빔 비비빔");
        menuEntity.setPrice(52000);

        MenuEntity result = menuRepository.save(menuEntity);

    }

}