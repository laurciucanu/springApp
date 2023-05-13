package com.example.springApp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private ResourceLoader resourceLoader;

    // Endpoint for accessing all the data
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allData() throws IOException {
        Resource dataResource = resourceLoader.getResource("classpath:products.json");
        InputStream inputStream = dataResource.getInputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing
        List<JsonItem> jsonItems = objectMapper.readValue(inputStream, new TypeReference<List<JsonItem>>() {
        });
        List<JsonItem> results = new ArrayList<>(jsonItems);

        // Convert the list of matching items to JSON format
        String jsonResult = objectMapper.writeValueAsString(results);

        return ResponseEntity.ok(jsonResult);
    }

    // Endpoint for searching a title the returning all of it's data
    @GetMapping(value = "/cauta/title", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> search(@RequestParam String title) throws IOException {
        List<JsonItem> results = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing

        // Load the JSON data from the file
        Resource dataResource = new ClassPathResource("products.json");
        InputStream inputStream = dataResource.getInputStream();

        // Convert the JSON data to a list of JsonItem objects
        List<JsonItem> items = objectMapper.readValue(inputStream, new TypeReference<List<JsonItem>>() {
        });

        // Search for items with matching titles
        for (JsonItem item : items) {
            if (item.getTitle().contains(title)) {
                results.add(item);
            }
        }

        // Convert the list of matching items to JSON format
        String jsonResult = objectMapper.writeValueAsString(results);

        return ResponseEntity.ok(jsonResult);
    }

    // Endpoint for returning all titles
    @GetMapping(value = "/title", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allTitles() throws IOException {
        Resource dataResource = resourceLoader.getResource("classpath:products.json");
        InputStream inputStream = dataResource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing
        List<JsonItem> jsonItems = objectMapper.readValue(inputStream, new TypeReference<List<JsonItem>>() {
        });
        List<String> results = new ArrayList<>();
        for (JsonItem item : jsonItems) {
            results.add(item.getTitle());
        }

        // Convert the list of matching items to JSON format
        String jsonResult = objectMapper.writeValueAsString(results);
        return ResponseEntity.ok(jsonResult);
    }
}