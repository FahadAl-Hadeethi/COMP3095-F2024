package ca.gbc.orderservice.service;

import ca.gbc.orderservice.client.InventoryClient;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Starting order placement for skuCode: {}, quantity: {}", orderRequest.skuCode(), orderRequest.quantity());

        try {
            // Check if product is in stock using InventoryClient
            boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
            log.info("Inventory check result for skuCode {}: {}", orderRequest.skuCode(), isProductInStock);

            if (isProductInStock) {
                // Build and save the order
                Order order = Order.builder()
                        .orderNumber(UUID.randomUUID().toString())
                        .price(orderRequest.price())
                        .skuCode(orderRequest.skuCode())
                        .quantity(orderRequest.quantity())
                        .build();

                orderRepository.save(order);
                log.info("Order placed successfully: {}", order.getOrderNumber());
            } else {
                log.error("Product with skuCode {} is not in stock.", orderRequest.skuCode());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with skuCode " + orderRequest.skuCode() + " is not in stock.");
            }
        } catch (Exception e) {
            log.error("Error while placing the order: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while placing the order. Please try again later.");
        }
    }
}