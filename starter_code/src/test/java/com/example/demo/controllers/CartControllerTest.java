package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void removeFromCartHappyPath() {

        Item item = new Item();
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Cart cart = new Cart();
        cart.setId(1L);

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(cartRepository.save(cart)).thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart result = response.getBody();
        assertNotNull(result);
        assertEquals(Long.valueOf(1), cart.getId());
        assertEquals(2, result.getItems().size());
        assertEquals(item, result.getItems().get(0));
        assertEquals(user, result.getUser());
        assertEquals(BigDecimal.valueOf(5.98), result.getTotal());

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        when(cartRepository.save(cart)).thenReturn(cart);

        response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        result = response.getBody();
        assertNotNull(result);
        assertEquals(Long.valueOf(1), cart.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(item, result.getItems().get(0));
        assertEquals(user, result.getUser());
        assertEquals(BigDecimal.valueOf(2.99), result.getTotal());
    }
}
