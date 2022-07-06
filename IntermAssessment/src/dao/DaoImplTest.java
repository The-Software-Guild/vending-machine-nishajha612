package dao;

import dto.Item;

import java.math.BigDecimal;

class DaoImplTest {

    private DaoImplTest testService;
    private Object testDao;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getAllItems() {
    }

    @org.junit.jupiter.api.Test
    void getItem() {
        Item snickersClone = new Item("Chocolate");
        snickersClone.setCost(new BigDecimal("2.10"));

        BigDecimal money = new BigDecimal("3.00");

        Item malteasersClone = new Item("Coca Cola");
        malteasersClone.setCost(new BigDecimal("2.10"));


    }
}

