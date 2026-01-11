package com.example.foodordering.menu.controller;

import com.example.foodordering.menu.dto.MenuItemRequest;
import com.example.foodordering.menu.dto.MenuItemResponse;
import com.example.foodordering.menu.service.MenuItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public MenuItemResponse create(@RequestBody MenuItemRequest request) {
        return menuItemService.create(request);
    }

    @GetMapping
    public List<MenuItemResponse> getAll() {
        return menuItemService.getAll();
    }

    @GetMapping("/by-category/{categoryId}")
    public List<MenuItemResponse> getByCategory(@PathVariable Long categoryId) {
        return menuItemService.getByCategory(categoryId);
    }
}
