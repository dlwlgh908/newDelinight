/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<MenuDTO> menuListByHotel(Long hotelNum) {
        List<MenuEntity> menuEntityList = menuRepository.findByStoreEntity_HotelEntity_Id(hotelNum);
        List<MenuDTO> menuDTOList = menuEntityList.stream().map(data -> modelMapper.map(data,MenuDTO.class)).toList();
        return menuDTOList;
    }
}
