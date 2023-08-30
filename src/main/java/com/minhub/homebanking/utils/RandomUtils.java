package com.minhub.homebanking.utils;


import java.util.Random;

public class RandomUtils {
    private static final String CARD_NUMBER_SEPARATOR = "-";
    private static final int CARD_NUMBER_SECTION_LENGTH = 4;

    private static final int MIN_ACCOUNT_NUMBER = 10000000;
    private static final int MAX_ACCOUNT_NUMBER = 99999999;

    private static final int MIN_CARD_SECTION = 1000;
    private static final int MAX_CARD_SECTION = 9999;

    public static int generateRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min must be less than or equal to max");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static String generateRandomAccountNumber() {
        int accountNumber = generateRandomNumber(MIN_ACCOUNT_NUMBER, MAX_ACCOUNT_NUMBER);
        return "VIN-" + accountNumber;
        }
        public static String generateRandomCardNumber() {
            StringBuilder cardNumber = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int section = generateRandomNumber(MIN_CARD_SECTION, MAX_CARD_SECTION);
                cardNumber.append(section);
                if (i < 3) {
                    cardNumber.append(CARD_NUMBER_SEPARATOR);
                }
            }
            return cardNumber.toString();
        }

        public static Integer generateRandomCvv() {
             return (int) (Math.random() * (999 - 100 + 1) + 100);
        }

    }

