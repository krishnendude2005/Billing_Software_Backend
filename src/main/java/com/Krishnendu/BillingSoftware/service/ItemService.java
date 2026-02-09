package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.ItemRequest;
import com.Krishnendu.BillingSoftware.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    ItemResponse add(ItemRequest request, MultipartFile file);
    List<ItemResponse> fetchItems();
    void deleteItem(String itemId);
}
