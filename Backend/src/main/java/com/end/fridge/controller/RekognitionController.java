package com.end.fridge.controller;

import com.end.fridge.service.RekognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rekognition")
@CrossOrigin
public class RekognitionController {

    @Autowired
    private RekognitionService rekognitionService;

    @PostMapping("/detectLabels")
    public ResponseEntity<List<String>> detectLabels(MultipartFile file) {
        try {
            List<String> labels = rekognitionService.detectLabels(file.getInputStream());
            return ResponseEntity.ok(labels);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
