package com.example.traning.controller;


import com.example.traning.entity.Order;
import com.example.traning.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @GetMapping ("order/id/{id}")
    public Order findOrderById (@PathVariable Long id){
       Optional<Order> orderOptional = orderRepository.findById(id);

        if(orderOptional.isPresent()){
            return orderOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found!");
        }
    }

    @GetMapping ("order/clientId/{clientId}")
    public List<Order> findOrderByClientId (@PathVariable Long clientId){
        List<Order> orderList = orderRepository.findByClientId(clientId);
        if(orderList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found!");
        } else {
            return orderList;
        }
    }

    @PostMapping("order")
    public Order addOrder(@RequestBody Long clientId){

        Order order = new Order();
        order.setClientId(clientId);

        return orderRepository.save(order);
    }

    @PutMapping("order/id/{id}")
    public Order putOrder (@PathVariable Long id, @RequestBody Long clientId){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            order.setClientId(clientId);
            return orderRepository.save(order);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found!");
        }
    }

    @DeleteMapping("order/id/{id}")
    public Order deleteOrder (@PathVariable Long id){
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            orderRepository.delete(order);
            return order;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found!");
        }
    }

}
