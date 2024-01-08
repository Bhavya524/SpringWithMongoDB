package com.example.demo.controller;

import com.example.demo.model.GroceryItem;
import com.example.demo.service.GroceryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/grocery")
public class GroceryController {
    @Autowired
    private GroceryService groceryService;

    @PostMapping
    public String save(@RequestBody GroceryItem groceryItem) {
        return groceryService.save(groceryItem);
    }

    @GetMapping
    public List<GroceryItem> getAllItems() {
        return groceryService.getAllItems();
    }

    @GetMapping(value = "{id}")
    public Optional<GroceryItem> getItemById(@PathVariable(name = "id") String id) {
        return groceryService.getById(id);
    }

    @GetMapping(value = "/category/{category}")
    public List<GroceryItem> getByCategory(@PathVariable(name = "category") String category) {
        return groceryService.findItemByCategory(category);
    }

    @GetMapping(value = "/cat-qua/{category}/{quantity}")
    public List<GroceryItem> getByCategory(@PathVariable(name = "category") String category, @PathVariable(name = "quantity") Long quantity) {
        return groceryService.findItemByCategoryAndQuantity(category, quantity);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(name = "id") String id) {
        groceryService.deleteById(id);
    }

    @GetMapping(value = "/search")
    public Page<GroceryItem> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(defaultValue = "2") Integer size
    ) {
        Pageable pageable = Pageable.ofSize(size);
        return groceryService.search(name, category, quantity, pageable);

    }


}
