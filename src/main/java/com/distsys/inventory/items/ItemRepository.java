package com.distsys.inventory.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    public Item findByName(String name);
    public List<Item> findByNameContaining(String keyword);
}
