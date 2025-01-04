package com.eventostec.api.domain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
public class AWSConfig {
    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Bean
    public AmazonS3 createS3Instance(){

        return AmazonS3ClientBuilder
            .standard()
            .withRegion(awsRegion)
            .build();
    }


}
