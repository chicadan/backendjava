package com.minhub.homebanking;

import com.minhub.homebanking.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardUtilsTests {



    @Test
    public void cardNumberIsCreated(){
        String cardNumber = RandomUtils.generateRandomCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void generateRandomCvv() {
        Integer cvv1 = RandomUtils.generateRandomCvv();

        assertNotNull(cvv1);
        assertTrue(cvv1 >= 100 && cvv1 <= 999);
    }

    @Test
    public void differentRandomCvv() {
        Integer cvv1 = RandomUtils.generateRandomCvv();
        Integer cvv2 = RandomUtils.generateRandomCvv();

        assertNotEquals(cvv1, cvv2);
    }


}
