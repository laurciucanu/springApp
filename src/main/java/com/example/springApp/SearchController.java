package com.example.springApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SearchController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/search")
    public ResponseEntity<List<JsonItem>> search(@RequestParam String title) throws IOException {
        Resource dataResource = resourceLoader.getResource("classpath:products.json");
        InputStream inputStream = dataResource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        List<JsonItem> jsonItems = mapper.readValue(inputStream, new TypeReference<List<JsonItem>>() {});
        List<JsonItem> results = new ArrayList<>();
        for (JsonItem item : jsonItems) {
            if (item.getTitle().equals(title)) {
                results.add(item);
            }
        }
        return ResponseEntity.ok(results);
    }
}