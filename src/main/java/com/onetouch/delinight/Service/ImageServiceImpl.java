package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.ImageDTO;
import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ImageServiceImpl implements ImageService {

    private final S3Service s3Service;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public String readStore(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findByStoreEntity_Id(id);
        if(imageEntity.isPresent()){
            return imageEntity.get().getFullUrl();
        }else {
            return "/img/defaultImg.png";
        }
    }

    @Override
    public String read(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findByMenuEntity_Id(id);//받은 이미지 넘버로 url 던지기
        if(imageEntity.isPresent()){
            return imageEntity.get().getFullUrl();
        }else {
            return "/img/defaultImg.png";
        }

    }

    @Override
    public Map<Long,String> register(MultipartFile multipartFile, String imgType) throws IOException, InterruptedException {

        UUID uuid = UUID.randomUUID();
        String originName = multipartFile.getOriginalFilename();
        String fileName = uuid+"_"+originName.substring(originName.lastIndexOf("/")+1);
        String fullUrl = "images/"+fileName;
        s3Service.upload(multipartFile, "images/"+fileName, imgType);
        fullUrl = "https://myimghouse.s3.ap-northeast-2.amazonaws.com/images/"+fileName;

        ImageDTO imageDTO = ImageDTO.builder().fullUrl(fullUrl).originName(originName).fileName(fileName).build();
        log.info(imageDTO);
        ImageEntity imageEntity = modelMapper.map(imageDTO, ImageEntity.class);
        ImageEntity afterImage = imageRepository.save(imageEntity);
        log.info(afterImage);
        Map<Long, String> result = new HashMap<>();
        log.info(afterImage.getFullUrl()+afterImage.getImgNum());
        result.put(afterImage.getImgNum(), afterImage.getFullUrl());
        log.info(result);
        return result;
    }

    @Override
    public void delete(Long imgNum) {
        Optional<ImageEntity> optionalImage = imageRepository.findById(imgNum);

        if(optionalImage.isPresent()){
            ImageEntity imageEntity = optionalImage.get();
            s3Service.deleteFile(imageEntity.getFullUrl());
            imageRepository.deleteById(imgNum);
        }
        else {
            log.info("이미지가 없습니다.");
        }
    }

    @Override
    public void update(MultipartFile multipartFile, Long imgNum, String imgType) throws IOException, InterruptedException {
        UUID uuid = UUID.randomUUID();
        String originName = multipartFile.getOriginalFilename();
        String fileName = uuid+"_"+originName.substring(originName.lastIndexOf("/")+1);
        String fullUrl = "images/"+fileName;
        s3Service.upload(multipartFile, "images/"+fileName, imgType);
        s3Service.deleteFile(fullUrl);
        fullUrl = "https://myimghouse.s3.ap-northeast-2.amazonaws.com/images/"+fileName;
        Optional<ImageEntity> optionalImage = imageRepository.findById(imgNum);
        if(optionalImage.isPresent()) {
            ImageEntity imageEntity = optionalImage.get();
            imageEntity.setFullUrl(fullUrl);
            imageEntity.setFileName(fileName);
            imageEntity.setOriginName(originName);
            ImageEntity afterImg = imageRepository.save(imageEntity);
            log.info(afterImg);
        }
        else {

            log.info("해당 아이디로 조회되는 이미지가 없습니다.");
        }
    }

    @Override
    public void dummyImgDelete() {

        //이미지를 업로드 하였으나 3시간 동안 해당 게시판의 글이 저장되지 않은 글 삭제(delete unsaved shipping image older than 3 hours)
        imageRepository.deleteByRegTimeIsLessThanEqualAndMenuEntityIsNullAndHotelEntityIsNull(LocalDateTime.now().minusHours(3));

    }
}
