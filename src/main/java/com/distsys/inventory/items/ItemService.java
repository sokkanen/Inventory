package com.distsys.inventory.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findItemsByKeyword(String keyword) {
        return itemRepository.findByNameContaining(keyword);
    }

    public Item findById(Long id) {
        Optional<Item> possibleItem = itemRepository.findById(id);
        return possibleItem.isPresent() ? possibleItem.get() : null;
    }

    @Transactional
    public Item modifyItem(Item item) {
        Item itemFromDb = itemRepository.findByName(item.getName());
        itemFromDb.setStock(item.getStock());
        itemFromDb.setPrice(item.getPrice());
        itemRepository.save(itemFromDb);
        return itemFromDb;
    }

    public List<VerifyDto> verifyOrder(List<OrderDto> dtos ) {
        return dtos.stream().map(dto -> {
            Item item = findById(dto.getId());
            return item == null ?
                    new VerifyDto(
                            dto.getId(),
                            0L,
                            0L,
                            VerifyDto.Status.UNKNOWN_ITEM
                    ) :
                    new VerifyDto(
                            dto.getId(),
                            dto.getAmount(),
                            item.getStock(),
                            item.getStock() >= dto.getAmount() ? VerifyDto.Status.OK : VerifyDto.Status.NOT_OK
                    );
        }).collect(Collectors.toList());
    }

    public List<VerifyDto> performOrder(List<OrderDto> dtos ) {
        return dtos.stream().map(dto -> {
            Item item = findById(dto.getId());
            if (item == null) {
                return new VerifyDto(dto.getId(), 0L, 0L, VerifyDto.Status.UNKNOWN_ITEM);
            }
            VerifyDto.Status status = item.getStock() >= dto.getAmount() ? VerifyDto.Status.OK : VerifyDto.Status.NOT_OK;
            if (status == VerifyDto.Status.OK) {
                item.setStock(item.getStock() - dto.getAmount());
                itemRepository.save(item);
            }
            return new VerifyDto(dto.getId(), dto.getAmount(), item.getStock(), status);
        }).collect(Collectors.toList());
    }
}
