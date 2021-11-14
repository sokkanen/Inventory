package com.distsys.inventory.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class OrderDto {

    private Long id;
    private Long amount;

}
