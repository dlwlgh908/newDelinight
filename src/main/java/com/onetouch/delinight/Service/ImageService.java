package com.onetouch.delinight.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageService {

    public Map<Long, String> register(MultipartFile multipartFile);
    public String read(Long id);

    public void delete(Long imgNum);

    public void update(MultipartFile multipartFile, Long num);
    public void dummyImgDelete();


}
