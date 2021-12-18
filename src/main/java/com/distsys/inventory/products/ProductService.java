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
    public Product save(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteItemById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findProductsByKeyword(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public Product findById(Long id) {
        Optional<Product> possibleProduct = productRepository.findById(id);
        return possibleProduct.isPresent() ? possibleProduct.get() : null;
    }

    @Transactional
    public Product modifyItem(Product product) {
        Product productFromDb = productRepository.findByName(product.getName());
        productFromDb.setStock(product.getStock());
        productFromDb.setPrice(product.getPrice());
        productRepository.save(productFromDb);
        return productFromDb;
    }

    public List<VerifyResult> verifyOrder(List<OrderDto> dtos ) {
        List<OrderDto> distinctDtos = mergeDuplicates(dtos);
        return distinctDtos.stream().map(dto -> {
            Product product = findById(dto.getId());
            return product == null ?
                    new VerifyResult(
                            dto.getId(),
                            "",
                            0L,
                            0L,
                            VerifyResult.Status.UNKNOWN_ITEM
                    ) :
                    new VerifyResult(
                            dto.getId(),
                            product.getName(),
                            dto.getAmount(),
                            product.getStock(),
                            product.getStock() >= dto.getAmount() ? VerifyResult.Status.OK : VerifyResult.Status.NOT_OK
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

    private void validateProduct(Product product) {
        // Product with stock of 0 can be added
        if (product.getName().isEmpty() || product.getPrice().equals(0)) {
            throw new ProductException();
        }
    }
}
