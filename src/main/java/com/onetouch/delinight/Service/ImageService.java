package com.onetouch.delinight.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public Long register(MultipartFile multipartFile);

    public void delete(Long imgNum);

    public void update(MultipartFile multipartFile, Long num);
    public void dummyImgDelete();

}
