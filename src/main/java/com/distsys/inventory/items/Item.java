package com.distsys.inventory.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor @AllArgsConstructor @Data
public class Item extends AbstractPersistable<Long> {
    @Column(unique=true)
    private String name;
    private Long stock;
    private Long price;
}
