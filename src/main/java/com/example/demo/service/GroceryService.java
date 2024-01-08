package com.example.demo.service;

import com.example.demo.model.GroceryItem;
import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroceryService {

    @Autowired
    private ItemRepository itemRepository;
    private MongoTemplate mongoTemplate;

    public GroceryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String save(GroceryItem groceryItem) {
        return itemRepository.save(groceryItem).getId();
    }

    public List<GroceryItem> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<GroceryItem> getById(String id) {
        return itemRepository.findById(id);
    }

    public List<GroceryItem> findItemByCategory(String category) {
        return itemRepository.getByCat(category);
    }

    public List<GroceryItem> findItemByCategoryAndQuantity(String category, Long quantity) {
        return itemRepository.getByCategoryAndQuantity(category, quantity);
    }

    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }

    public List<GroceryItem> findByCategory(String category) {
        Query query = new Query(Criteria.where("category").is(category));
        return mongoTemplate.find(query, GroceryItem.class);
    }

    public List<GroceryItem> findByCategoryAndQuantity(String category, Long quantity) {
        Query query = new Query(Criteria.where("category").is(category).and("quantity").is(quantity));
        return mongoTemplate.find(query, GroceryItem.class);
    }

    public Page<GroceryItem> search(String name, String category, Integer quantity, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();
        if (null != name && null != quantity && null != category) {
            criteria.add(Criteria.where("name").regex(name, "i"));
            criteria.add(Criteria.where("quantity").gt(quantity));
            criteria.add(Criteria.where("category").regex(category, "i"));
        }
        if (null != criteria) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(criteria.toArray(new Criteria[0]))));
        }
        Page<GroceryItem> groceryItems = PageableExecutionUtils
                .getPage(mongoTemplate.find(query, GroceryItem.class), pageable,
                        () -> mongoTemplate.count(query.skip(0).limit(0), GroceryItem.class));
        return groceryItems;
    }
}
