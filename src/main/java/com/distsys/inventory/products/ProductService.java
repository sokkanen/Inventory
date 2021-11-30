package com.distsys.inventory.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product save(Product item) {
        return productRepository.save(item);
    }

    @Transactional
    public void deleteItemById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findItemsByKeyword(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public Product findById(Long id) {
        Optional<Product> possibleItem = productRepository.findById(id);
        return possibleItem.isPresent() ? possibleItem.get() : null;
    }

    @Transactional
    public Product modifyItem(Product item) {
        Product itemFromDb = productRepository.findByName(item.getName());
        itemFromDb.setStock(item.getStock());
        itemFromDb.setPrice(item.getPrice());
        productRepository.save(itemFromDb);
        return itemFromDb;
    }

    public List<VerifyResult> verifyOrder(List<OrderDto> dtos ) {
        List<OrderDto> distinctDtos = mergeDuplicates(dtos);
        return distinctDtos.stream().map(dto -> {
            Product item = findById(dto.getId());
            return item == null ?
                    new VerifyResult(
                            dto.getId(),
                            "",
                            0L,
                            0L,
                            VerifyResult.Status.UNKNOWN_ITEM
                    ) :
                    new VerifyResult(
                            dto.getId(),
                            item.getName(),
                            dto.getAmount(),
                            item.getStock(),
                            item.getStock() >= dto.getAmount() ? VerifyResult.Status.OK : VerifyResult.Status.NOT_OK
                    );
        }).collect(Collectors.toList());
    }

    public List<VerifyResult> performOrder(List<OrderDto> dtos ) {
        List<OrderDto> distinctDtos = mergeDuplicates(dtos);
        return distinctDtos.stream().map(dto -> {
            Product item = findById(dto.getId());
            if (item == null) {
                return new VerifyResult(dto.getId(), "", 0L, 0L, VerifyResult.Status.UNKNOWN_ITEM);
            }
            VerifyResult.Status status = item.getStock() >= dto.getAmount() ? VerifyResult.Status.OK : VerifyResult.Status.NOT_OK;
            if (status == VerifyResult.Status.OK) {
                item.setStock(item.getStock() - dto.getAmount());
                productRepository.save(item);
            }
            return new VerifyResult(dto.getId(), item.getName(), dto.getAmount(), item.getStock(), status);
        }).collect(Collectors.toList());
    }

    private List<OrderDto> mergeDuplicates(List<OrderDto> dtos) {
        List<Long> ids = dtos
                .stream()
                .map(d -> d.getId())
                .distinct()
                .collect(Collectors.toList());
        List<OrderDto> distinctDtos = new ArrayList<>();
        ids.stream().forEach(id -> {
            Long amount = dtos.stream().filter(d -> d.getId() == id).mapToLong(d -> d.getAmount()).sum();
            distinctDtos.add(new OrderDto(id, amount));
        });
        return distinctDtos;
    }
}
