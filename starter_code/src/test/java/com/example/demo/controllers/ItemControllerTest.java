package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsHappyPath() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        List<Item> items = Collections.singletonList(item);

        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> result = response.getBody();
        assertNotNull(result);
        assertEquals(Long.valueOf(1), result.get(0).getId());
        assertEquals("Round Widget", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(2.99), result.get(0).getPrice());
        assertEquals("A widget that is round", result.get(0).getDescription());
        assertEquals(1, result.size());
    }

    @Test
    public void getItemByIdHappyPath() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item result = response.getBody();
        assertNotNull(result);
        assertEquals(Long.valueOf(1), result.getId());
        assertEquals("Round Widget", result.getName());
        assertEquals(BigDecimal.valueOf(2.99), result.getPrice());
        assertEquals("A widget that is round", result.getDescription());
    }

    @Test
    public void getItemsByNameHappyPath() {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");
        List<Item> items = Collections.singletonList(item);

        when(itemRepository.findByName("Round Widget")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> result = response.getBody();
        assertNotNull(result);
        assertEquals(Long.valueOf(1), result.get(0).getId());
        assertEquals("Round Widget", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(2.99), result.get(0).getPrice());
        assertEquals("A widget that is round", result.get(0).getDescription());
        assertEquals(1, result.size());
    }
}
