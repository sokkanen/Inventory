package com.distsys.inventory.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class VerifyDto {
    private Long id;
    private Long amount;
    private Long available;
    private Status status;

    enum Status {
        OK,
        NOT_OK,
        UNKNOWN_ITEM
    }
}


