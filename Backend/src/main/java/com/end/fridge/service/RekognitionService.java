package com.end.fridge.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    private final AmazonRekognition rekognitionClient;

    public RekognitionService(@Value("${aws.accessKey}") String accessKey,
                              @Value("${aws.secretKey}") String secretKey) {
        this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public List<String> detectLabels(InputStream imageStream) {
        ByteBuffer imageBytes;
        try {
            imageBytes = ByteBuffer.wrap(imageStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error reading image", e);
        }

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(imageBytes));

        DetectLabelsResult result = rekognitionClient.detectLabels(request);
        return result.getLabels().stream().map(Label::getName).collect(Collectors.toList());
    }
}
