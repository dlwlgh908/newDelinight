package com.onetouch.delinight.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {


    @Value("myimghouse")
    private String bucket;
    private final S3Client s3Client;

    public String upload(MultipartFile multipartFile, String fullUrl) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(fullUrl).build();
            // 집어넣는 요청에 버킷 이름, 파일 키 전달 put Object 객체를 집어 넣을땐 실제 파일 바이트를 읽고 써 요청(메타데이터와 비슷)과 함께 전달
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
            return "저장 성공";
        } catch (IOException e) {
            e.printStackTrace(); // 오류 로그 프린트
            return "저장 실패";
        }
    }


    public String deleteFile(String fullUrl) {
        String key = fullUrl.split(".com/")[1];
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        try {
            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);
            return "삭제 성공";
        } catch (S3Exception e) {
            log.error("S3 객체 삭제 중 오류 발생: {}", e.awsErrorDetails().errorMessage(), e);
            return "삭제 실패";
        }
    }
}
