package com.budjb.eks.ststest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/")
    void doStuff() {
        S3Client client = S3Client.builder().credentialsProvider(WebIdentityTokenFileCredentialsProvider.create()).build();

        client.createBucket(CreateBucketRequest.builder().bucket("bud-test-bucket").build());
        client.putObject(PutObjectRequest.builder().bucket("bud-test-bucket").key("foo").build(), RequestBody.fromString("bar"));

        ListObjectsResponse listObjectsResponse = client.listObjects(ListObjectsRequest.builder().bucket("bud-test-bucket").build());
        listObjectsResponse.contents().forEach(i -> System.out.println(i.key()));
    }
}
