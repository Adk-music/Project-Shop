package com.example.traning.controller;

import com.example.traning.dto.ProductDto;
import com.example.traning.entity.Client;
import com.example.traning.entity.Product;
import com.example.traning.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("product/id/{id}")
    public Product getProductById(@PathVariable Long id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            return productOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!");
        }
    }

    @GetMapping("product/name/{name}")
    public Product getProductByName(@PathVariable String name){
        Optional<Product> productOptional = productRepository.findByName(name);
        if(productOptional.isPresent()){
            return productOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!");
        }
    }


    @PostMapping("product")
    public Product addProduct(@RequestBody ProductDto productDto){

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        return productRepository.save(product);
    }

    @PutMapping("product/id/{id}")
    public Product putProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            return productRepository.save(product);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!");
        }
    }

    @DeleteMapping("product/id/{id}")
    public Product deleteProduct (@PathVariable Long id){
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            productRepository.delete(product);
            return product;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!");
        }
    }
    

}
