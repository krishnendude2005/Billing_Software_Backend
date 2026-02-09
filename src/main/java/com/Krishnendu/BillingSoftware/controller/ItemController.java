package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.ItemRequest;
import com.Krishnendu.BillingSoftware.service.impl.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemService;


    @PostMapping("/admin/items")
    public ResponseEntity<?> addItem(@RequestPart String itemRequestString,
                                     @RequestPart MultipartFile file) {

        // Map the incoming ItemRequest String to the ItemRequest class
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest itemRequest = null;
        try{
            itemRequest = objectMapper.readValue(itemRequestString, ItemRequest.class);
        }catch(Exception ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("ItemRequest string is invalid");
        }

        itemService.add(itemRequest, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Successfully added Item");

    }

    @GetMapping("/items")
    public ResponseEntity<?> getItems() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.fetchItems());
    }

    @DeleteMapping("/admin/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemId) {

        itemService.deleteItem(itemId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Successfully deleted Item");
    }
}
