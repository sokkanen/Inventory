package com.distsys.inventory.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class VerifyResult {
    private Long id;
    private String name;
    private Long amount;
    private Long available;
    private Status status;

    enum Status {
        OK,
        NOT_OK,
        UNKNOWN_ITEM
    }
}


