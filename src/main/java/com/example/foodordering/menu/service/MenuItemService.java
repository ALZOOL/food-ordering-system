package com.example.foodordering.menu.service;

import com.example.foodordering.menu.dto.MenuItemRequest;
import com.example.foodordering.menu.dto.MenuItemResponse;
import com.example.foodordering.menu.entity.Category;
import com.example.foodordering.menu.entity.MenuItem;
import com.example.foodordering.menu.repository.CategoryRepository;
import com.example.foodordering.menu.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    public MenuItemResponse create(MenuItemRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);

        MenuItem savedItem = menuItemRepository.save(item);

        return mapToResponse(savedItem);
    }

    public List<MenuItemResponse> getAll() {
        List<MenuItem> items = menuItemRepository.findAll();
        List<MenuItemResponse> responses = new ArrayList<>();

        for (MenuItem item : items) {
            responses.add(mapToResponse(item));
        }

        return responses;
    }

    public List<MenuItemResponse> getByCategory(Long categoryId) {
        List<MenuItem> items = menuItemRepository.findByCategoryId(categoryId);
        List<MenuItemResponse> responses = new ArrayList<>();

        for (MenuItem item : items) {
            responses.add(mapToResponse(item));
        }

        return responses;
    }

    private MenuItemResponse mapToResponse(MenuItem item) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setCategoryName(item.getCategory().getName());
        return response;
    }
}
