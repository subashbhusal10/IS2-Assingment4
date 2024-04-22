package com.hidemessage.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hidemessage.utils.SteganographyUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Post")
public class PostRoute {

    @PostMapping(
            value = "/posts/create",
            consumes = {"multipart/form-data"}
    )
    public String createPost(
            @RequestParam("file") MultipartFile file,
            @RequestParam("message") MultipartFile message,
            @RequestPart("otherData") String otherDataJson

    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> otherData = objectMapper.readValue(otherDataJson, Map.class);

        Integer l = Integer.parseInt(otherData.get("L").toString());
        int l1 = 0;
        int l2 = 0;
        int startingBitNo;

        if (otherData.get("l1") != null) {
            l1 = Integer.parseInt(otherData.get("l1").toString());
        }

        if (otherData.get("l2") != null) {
            l2 = Integer.parseInt(otherData.get("l2").toString());
        }

        if (otherData.get("startingBitNo") != null && !otherData.get("startingBitNo").toString().isEmpty()) {
            startingBitNo = Integer.parseInt(otherData.get("startingBitNo").toString());
        } else {
            startingBitNo = 0;
        }

        SteganographyUtils.applyMessageAndSaveFile(file, message, l, l1, l2, startingBitNo);

        return "File uploaded successfully!";
    }


    @GetMapping("/posts")
    public List<String> getPosts() throws IOException {
        return Files.list(Paths.get("data/posts"))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }

}
