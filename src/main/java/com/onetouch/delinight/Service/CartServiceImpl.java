/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Menu;
import com.onetouch.delinight.DTO.CartDTO;
import com.onetouch.delinight.DTO.CartItemDTO;
import com.onetouch.delinight.DTO.MenuDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.CartEntity;
import com.onetouch.delinight.Entity.CartItemEntity;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Repository.CartItemRepository;
import com.onetouch.delinight.Repository.CartRepository;
import com.onetouch.delinight.Repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    @Override
    public String cartToOrder(Long cartNum) {
        return null;
    }

    @Override
    public String minusQuantity(Long cartItemNum) {
        Optional<CartItemEntity> optionalCartItemEntity = cartItemRepository.findById(cartItemNum);
        if(optionalCartItemEntity.isPresent()){
            CartItemEntity entity = optionalCartItemEntity.get();
            if(entity.getQuantity()==1){
                return "더 이상 수량을 줄일 수 없습니다.";
            }
            else {
                entity.setQuantity(entity.getQuantity() - 1);
                cartItemRepository.save(entity);
                return "수량을 줄였습니다.";
            }
        }
        else{
            return "오류 발생";
        }    }

    @Override
    public String plusQuantity(Long cartItemNum) {
        Optional<CartItemEntity> optionalCartItemEntity = cartItemRepository.findById(cartItemNum);
        if(optionalCartItemEntity.isPresent()){
            CartItemEntity entity = optionalCartItemEntity.get();
            entity.setQuantity(entity.getQuantity()+1);
            cartItemRepository.save(entity);
            return "수량을 늘렸습니다.";
        }
        else{
            return "오류 발생";
        }
    }

    @Override
    public void remove(Long cartItemNum) {
        cartItemRepository.deleteById(cartItemNum);
    }

    @Override
    public String clear(Long cartNum) {
        cartItemRepository.deleteByCartEntity_Id(cartNum);
        return "카트 비우기 성공";
    }

    @Override
    public List<CartItemDTO> list(Long cartNum) {
        List<CartItemEntity> entities = cartItemRepository.findByCartEntity_Id(cartNum);
        List<CartItemDTO> cartItemDTOList = entities.stream().map(data->modelMapper.map(data,CartItemDTO.class)
                .setCartDTO(modelMapper.map(data.getCartEntity(), CartDTO.class))
                .setMenuDTO(modelMapper.map(data.getMenuEntity(),MenuDTO.class).setStoreDTO(modelMapper.map(data.getMenuEntity().getStoreEntity(), StoreDTO.class))
                )).toList();
        return cartItemDTOList;
    }

    @Override
    public Integer add(Long cartNum, Long menuNum) {
        MenuEntity selectedMenu = menuRepository.findById(menuNum).get();
        CartEntity cartEntity = cartRepository.findById(cartNum).get();
        Optional<CartItemEntity> optionalCartItemEntity = cartItemRepository.findByCartEntity_IdAndMenuEntity_Id(cartEntity.getId(), selectedMenu.getId());
        if(optionalCartItemEntity.isPresent()){
            return 2;
        }
        else {
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setMenuEntity(selectedMenu);
            cartItemEntity.setCartEntity(cartEntity);
            cartItemEntity.setQuantity(1L);
            cartItemRepository.save(cartItemEntity);
            return 1;
        }
    }
}
