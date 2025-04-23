/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{
    private final MembersRepository membersRepository;
    private final ModelMapper modelMapper;
   private final PasswordEncoder passwordEncoder;
   private final StoreService storeService;
   private final HotelService hotelService;
   private final CenterRepository centerRepository;
   private final HotelRepository hotelRepository;
   private final CenterService centerService;

    @Override
    public MembersDTO findByEmail(String email) {

        Optional<MembersEntity> membersEntity = membersRepository.findByEmail2(email);

        if(membersEntity.isPresent()){
            MembersDTO membersDTO = modelMapper.map(membersEntity.get(), MembersDTO.class);
            return membersDTO;
        }
        else {
            MembersDTO membersDTO = new MembersDTO().builder().email("ghost").build();
            return membersDTO;
        }

    }

    @Override
    public void create(MembersDTO membersDTO) {

        MembersEntity membersEntity =
            modelMapper.map(membersDTO, MembersEntity.class);
        String password = passwordEncoder.encode(membersDTO.getPassword());
        membersEntity.setRole(membersDTO.getRole());
        membersEntity.setStatus(Status.WAIT);
        membersEntity.setPassword(password);

        if(membersDTO.getRole().equals(Role.ADMIN)){
            Optional<CenterEntity> optionalCenterEntity = centerRepository.findById(membersDTO.getParentId());
            optionalCenterEntity.ifPresent(membersEntity::setCenterEntity);
        }
        if(membersDTO.getRole().equals(Role.STOREADMIN)){
            Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(membersDTO.getParentId());
            optionalHotelEntity.ifPresent(membersEntity::setHotelEntity);
        }
            membersRepository.save(membersEntity);


    }

    @Override
    public MembersDTO update(MembersDTO membersDTO) {
        Optional<MembersEntity> optionalMembersEntity
                = membersRepository.findById(membersDTO.getId());

        MembersEntity membersEntity = optionalMembersEntity.get();
        membersEntity.setPhone(membersDTO.getPhone());
        membersEntity.setPassword(membersDTO.getPassword());
        if (membersDTO.getPassword() == null){

        }
        return null;
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

    //@Override
    //public List<MembersDTO> findSuper() {
    //    List<MembersEntity> membersEntityList = membersRepository.selectSuperAd();
    //
    //    List<MembersDTO> membersDTOList =
    //    membersEntityList.stream().toList().stream().map(
    //            membersEntity -> modelMapper.map(membersEntity, MembersDTO.class)
    //    ).collect(Collectors.toList());
    //
    //    return membersDTOList;
    //}

    @Override
    public List<MembersDTO> findHotelAd() {
        return List.of();
    }

    @Override
    public List<MembersDTO> findStoreAd() {
        return List.of();
    }

    @Override
    public Page<MembersEntity> findAccount(Status status, int page, String email, String sep) {

        if(sep.equals("hotel")){

            Long id = centerService.findCenter(email);
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
            return membersRepository.selectHotelAdByStatus(status,pageable, id);

        }
        else {
            Long id = hotelService.findHotelByEmail(email);
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
            return membersRepository.selectStoreAdByStatus(status,pageable, id);

        }


    }


    //
    //@Override
    //public Page<MembersEntity> findStoreAd(Status status, int page) {
    //
    //
    //    List<Sort.Order> sorts = new ArrayList<>();
    //    sorts.add(Sort.Order.desc("id"));
    //    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    //    return this.membersRepository.selectStoreAd(pageable);
    //
    //}

    @Override
    public Page<MembersEntity> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectSuperAd(pageable);
    }

    @Override
    public Page<MembersEntity> getListHotel(int page, String email) {
        Long id = centerService.findCenter(email);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectHotelAd(pageable,id);
    }

    @Override
    public Page<MembersEntity> getListStore(int page, String email) {
        Long id = hotelService.findHotelByEmail(email);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectStoreAd(pageable, id);
    }

    @Override
    public Page<MembersEntity> getListBystatus(Status status, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectSuperAdByStatus(status,pageable);
    }

    @Override
    public MembersDTO approve(Long id) {

        MembersEntity membersEntity =
        membersRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        membersEntity.setStatus(Status.VALID);

        MembersDTO membersDTO =
            modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public MembersDTO Disapprove(Long id) {

        MembersEntity membersEntity =
                membersRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        membersEntity.setStatus(Status.NOTVALID);

        MembersDTO membersDTO =
                modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public Role findOnlyRoleByEmail(String email) {
        Role role = membersRepository.findByEmail(email).getRole();
        return role;
    }

    @Override
    public Map<Role, Long> findRoleByEmail(String email) {

        MembersEntity membersEntity = membersRepository.findByEmail(email);
        if(membersEntity.getRole().equals(Role.STOREADMIN)){
            Long id = storeService.findStoreByEmail(email);
            return Map.of(Role.STOREADMIN, id);
        }
        else if(membersEntity.getRole().equals(Role.ADMIN)){
            Long id = hotelService.findHotelByEmail(email);
            return Map.of(Role.ADMIN, id);
        }

        Long id = centerService.findCenter(email);
        return Map.of(Role.SUPERADMIN, id);
    }


}
