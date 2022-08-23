package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void getOrdersForUserHappyPath() {

        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(BigDecimal.valueOf(5.98));

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername("test")).thenReturn(user);

        UserOrder order = new UserOrder();
        order.setItems(Collections.singletonList(item));
        order.setUser(user);
        order.setTotal(BigDecimal.valueOf(5.98));
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(item, result.getItems().get(0));
        assertEquals(BigDecimal.valueOf(5.98), result.getTotal());
        assertEquals(user, result.getUser());

        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(order));
        ResponseEntity<List<UserOrder>> newResponse = orderController.getOrdersForUser("test");
        assertNotNull(newResponse);
        assertEquals(200, newResponse.getStatusCodeValue());

        List<UserOrder> newResult = newResponse.getBody();
        assertNotNull(newResult);
        assertEquals(1, newResult.get(0).getItems().size());
        assertEquals(item, newResult.get(0).getItems().get(0));
        assertEquals(BigDecimal.valueOf(5.98), newResult.get(0).getTotal());
        assertEquals(user, newResult.get(0).getUser());
        assertEquals(1, newResult.size());
    }
}
