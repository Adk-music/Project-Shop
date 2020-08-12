package com.example.traning.controller;

import com.example.traning.dto.DeleteOrderDetailsDto;
import com.example.traning.dto.FullOrderDetailDto;
import com.example.traning.dto.OrderDetailsDto;
import com.example.traning.dto.OrderProductDto;
import com.example.traning.entity.Client;
import com.example.traning.entity.Order;
import com.example.traning.entity.OrderDetails;
import com.example.traning.entity.Product;
import com.example.traning.repository.ClientRepository;
import com.example.traning.repository.OrderDetailsRepository;
import com.example.traning.repository.OrderRepository;
import com.example.traning.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderDetailsController {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;


    public OrderDetailsController(OrderDetailsRepository orderDetailsRepository,
                                  OrderRepository orderRepository,
                                  ProductRepository productRepository,
                                  ClientRepository clientRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("orderDetails/orderId/{orderId}")
    public FullOrderDetailDto findOrderDetailsById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found!");
        }
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByOrderId(orderId);
        for (OrderDetails orderDetail : orderDetailsList) {
            Optional<Product> productOptional = productRepository.findById(orderDetail.getProductId());
            if (productOptional.isPresent()) {
                OrderProductDto orderProductDto = new OrderProductDto();
                orderProductDto.setCount(orderDetail.getTotalCount());
                orderProductDto.setProductName(productOptional.get().getName());
                orderProductDtoList.add(orderProductDto);
            }
        }
        Optional<Client> optionalClient = clientRepository.findById(orderOptional.get().getClientId());
        if (optionalClient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        FullOrderDetailDto result = new FullOrderDetailDto();
        result.setOrderId(orderId);
        result.setClientName(optionalClient.get().getName());
        result.setProducts(orderProductDtoList);
        return result;
    }

    @PostMapping("orderDetails")
    public OrderDetails addOrderDetails(@RequestBody OrderDetailsDto orderDetailsDto) {

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(orderDetailsDto.getOrderId());
        orderDetails.setProductId(orderDetailsDto.getProductId());
        orderDetails.setTotalCount(orderDetailsDto.getTotalCount());

        return orderDetailsRepository.save(orderDetails);

    }

    @PutMapping("orderDetails")
    public OrderDetails updateOrderDetails(@RequestBody OrderDetailsDto orderDetailsDto) {

        Optional<OrderDetails> orderDetailsOptional =
                orderDetailsRepository.findByOrderIdAndProductId(orderDetailsDto.getOrderId(), orderDetailsDto.getProductId());
        OrderDetails orderDetails;
        if (orderDetailsOptional.isPresent()) {
            orderDetails = orderDetailsOptional.get();
        } else {
            orderDetails = new OrderDetails();
            orderDetails.setOrderId(orderDetailsDto.getOrderId());
            orderDetails.setProductId(orderDetailsDto.getProductId());

        }
        orderDetails.setTotalCount(orderDetailsDto.getTotalCount());
        return orderDetailsRepository.save(orderDetails);
    }

    @DeleteMapping("orderDetails")
    public OrderDetails deleteOrderDetails(@RequestBody DeleteOrderDetailsDto deleteOrderDetailsDto) {
        Optional<OrderDetails> orderDetailsOptional =
                orderDetailsRepository.findByOrderIdAndProductId(deleteOrderDetailsDto.getOrderId(), deleteOrderDetailsDto.getProductId());
        if (orderDetailsOptional.isPresent()) {
            OrderDetails orderDetail = orderDetailsOptional.get();
            orderDetailsRepository.delete(orderDetail);
            return orderDetail;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Details not found!");
        }
    }

}
