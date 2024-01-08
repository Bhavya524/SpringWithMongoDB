package com.example.demo.repository;

import com.example.demo.model.GroceryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ItemRepository extends MongoRepository<GroceryItem, String> {

    List<GroceryItem> findByCategoryAndQuantity(String category, Long quantity);

    @Query("{'category' : ?0}")
    List<GroceryItem> getByCat(String category);

    @Query("{'category' : ?0    , 'quantity' : ?1}")
    List<GroceryItem> getByCategoryAndQuantity(String category, Long quantity);

}
