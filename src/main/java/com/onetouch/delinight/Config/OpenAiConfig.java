package com.onetouch.delinight.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.print.attribute.standard.PrinterURI;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "openai")
public class OpenAiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;
    private String url = "https://api.openai.com/v1/chat/completions";

}
