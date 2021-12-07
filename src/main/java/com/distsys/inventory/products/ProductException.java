package com.distsys.inventory.products;

public class ProductException extends RuntimeException {
    public ProductException() {
        super("Invalid product");
    }
}
