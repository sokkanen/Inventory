package com.distsys.inventory.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("")
    public ResponseEntity listItems() {
        List<Product> items = productService.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestParam String keyword) {
        List<Product> items = productService.findItemsByKeyword(keyword);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getSingleItem(@PathVariable Long id) {
        Product item = productService.findById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * Verifies an order.
     * @param orderItems List of OrderDto's
     * @return A List of VerifyDto's
     */
    @PostMapping("/verify")
    public ResponseEntity<List<VerifyResult>> verifyOrder(@RequestBody List<OrderDto> orderItems) {
        List<VerifyResult> statuses = productService.verifyOrder(orderItems);
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    /**
     * Performs an order.
     * @param orderItems List of OrderDto's
     * @return A List of VerifyDto's
     */
    @PostMapping("/order")
    public ResponseEntity<List<VerifyResult>> performOrder(@RequestBody List<OrderDto> orderItems) {
        List<VerifyResult> statuses = productService.performOrder(orderItems);
        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity createNewItem(@RequestBody Product item) {
        try {
            Product product = productService.save(item);
            return new ResponseEntity<>(product == null ? HttpStatus.CONFLICT : HttpStatus.CREATED);
        } catch (Exception error) {
            System.out.println(error.getMessage());
            return new ResponseEntity<>(error.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /**
     * Sets existing item's price and / or amount
     * @param id Item id
     * @param item Item Object with new amount and price
     * @return Item with set parameters
     */
    @PutMapping("/{id}")
    public ResponseEntity setItemPriceAndAmount(@PathVariable Long id, @RequestBody Product item) {
        Product modifiedItem = productService.modifyItem(item);
        return new ResponseEntity<>(modifiedItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeSingleItem(@PathVariable Long id) {
        productService.deleteItemById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
