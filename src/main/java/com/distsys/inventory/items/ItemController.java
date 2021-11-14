package com.distsys.inventory.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ItemController {

    @Autowired
    ItemService itemService;

    @GetMapping("")
    public ResponseEntity listItems() {
        List<Item> items = itemService.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestParam String keyword) {
        List<Item> items = itemService.findItemsByKeyword(keyword);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getSingleItem(@PathVariable Long id) {
        Item item = itemService.findById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * Verifies an order.
     * @param orderItems List of OrderDto's
     * @return A List of VerifyDto's
     */
    @PostMapping("/verify")
    public ResponseEntity<List<VerifyDto>> verifyOrder(@RequestBody List<OrderDto> orderItems) {
        List<VerifyDto> statuses = itemService.verifyOrder(orderItems);
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    /**
     * Performs an order.
     * @param orderItems List of OrderDto's
     * @return A List of VerifyDto's
     */
    @PostMapping("/order")
    public ResponseEntity<List<VerifyDto>> performOrder(@RequestBody List<OrderDto> orderItems) {
        List<VerifyDto> statuses = itemService.performOrder(orderItems);
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity createNewItem(@RequestBody Item item) {
        itemService.save(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Sets existing item's price and / or amount
     * @param id Item id
     * @param item Item Object with new amount and price
     * @return Item with set parameters
     */
    @PutMapping("/{id}")
    public ResponseEntity setItemPriceAndAmount(@PathVariable Long id, @RequestBody Item item) {
        Item modifiedItem = itemService.modifyItem(item);
        return new ResponseEntity<>(modifiedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeSingleItem(@PathVariable Long id) {
        itemService.deleteItemById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
