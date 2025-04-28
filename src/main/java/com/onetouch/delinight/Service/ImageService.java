package com.onetouch.delinight.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageService {

    public Map<Long, String> register(MultipartFile multipartFile, String imgType) throws IOException, InterruptedException;
    public String read(Long id);
    public String readStore(Long id);
    public String readHotel(Long id);


    public void delete(Long imgNum);

    public void update(MultipartFile multipartFile, Long num, String imgType) throws IOException, InterruptedException;
    public void dummyImgDelete();
    public boolean ExistStoreImgByEmail(String email);
    public boolean ExistHotelImgByEmail(String email);



}
