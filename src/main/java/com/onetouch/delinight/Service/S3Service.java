package com.onetouch.delinight.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {


    @Value("myimghouse")
    private String bucket;
    private final S3Client s3Client;

    // 원래 http->spring->s3 저장 -> http 호출 이었으나
    // AWS lambda를 거쳐 일정한 사이즈로 리사이징을 하므로 S3 레이어가 아닌 Lambda 레이어로 http 통신이 필요함
    // 기존거는 주석 처리 후 인코딩해서 http 프로토콜로 post 통신 할 함수 하나 생성
    // 아예 지우려했으나 생각해보니 삭제 시 스프링에서 접근하니 delete는 필요함

    // 2차 수정 람다로 하려 했으나 파이썬 3.11 내 pillow 인식 불가로 직접 도커로 패키징해서 람다 업로드 했음에도 안되어 시간 부족으로 자바단에서 리사이징
    // 원복 및 람다 관련 메소드들 주석 처리

//    public String upload(MultipartFile multipartFile, String imageName, String imgType)throws IOException, InterruptedException{
//
//        String lambdaUrl = "https://r4gdeq3re5.execute-api.ap-northeast-2.amazonaws.com/default/lastResizing"; // 람다 엔드포인트
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("file", Base64.getEncoder().encodeToString(multipartFile.getBytes()));
//        body.put("resizeType", imgType);
//        body.put("imageName", imageName);
//
//        String requestBody = objectMapper.writeValueAsString(body);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(lambdaUrl))
//                .header("Content-Type", "application/json")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Lambda 호출 실패: " + response.body());
//        }
//
//
//
//        return null;
//    }
    public String upload(MultipartFile multipartFile, String fullUrl, String imgType) throws IOException {

        byte[] resizedImage = reSizing(multipartFile, imgType);


        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(fullUrl).build();
        // 집어넣는 요청에 버킷 이름, 파일 키 전달 put Object 객체를 집어 넣을땐 실제 파일 바이트를 읽고 써 요청(메타데이터와 비슷)과 함께 전달
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(resizedImage));
        return "저장 성공";
    }

    public byte[] reSizing(MultipartFile multipartFile, String imgType) throws IOException {

        int width = 600;
        int height;
        if(imgType.equals("square")){
            height = 600;
        }else {
            height = 200;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(multipartFile.getInputStream()).forceSize(width, height).outputFormat("jpg").toOutputStream(byteArrayOutputStream);

        byte[] resizedImage = byteArrayOutputStream.toByteArray();

        return  resizedImage;
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
