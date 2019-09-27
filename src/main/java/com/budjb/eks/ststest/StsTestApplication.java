package com.budjb.eks.ststest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
@SpringBootApplication
public class StsTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(StsTestApplication.class, args);
    }

    @PostMapping("/")
    void doStuff(@org.springframework.web.bind.annotation.RequestBody String bucketName) {
        S3Client client = S3Client.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build();

        client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        client.putObject(PutObjectRequest.builder().bucket(bucketName).key("foo").build(), RequestBody.fromString("bar"));

        ListObjectsResponse listObjectsResponse = client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build());
        listObjectsResponse.contents().forEach(i -> System.out.println(i.key()));
    }
}
