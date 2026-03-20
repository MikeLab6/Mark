package com.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MD5UtilTest {

    @Test
    void testEncrypt() {
        String password = "123456";
        String encrypted = MD5Util.encrypt(password);

        assertNotNull(encrypted);
        assertEquals(32, encrypted.length());
        assertEquals("e10adc3949ba59abbe56e057f20f883e", encrypted.toLowerCase());
    }

    @Test
    void testEncryptEmptyString() {
        String encrypted = MD5Util.encrypt("");
        assertNotNull(encrypted);
        assertEquals(32, encrypted.length());
    }

    @Test
    void testEncryptNull() {
        assertThrows(NullPointerException.class, () -> {
            MD5Util.encrypt(null);
        });
    }

    @Test
    void testVerifySuccess() {
        String password = "123456";
        String encrypted = MD5Util.encrypt(password);

        assertTrue(MD5Util.verify(password, encrypted));
    }

    @Test
    void testVerifyFail() {
        String password = "123456";
        String wrongPassword = "654321";
        String encrypted = MD5Util.encrypt(password);

        assertFalse(MD5Util.verify(wrongPassword, encrypted));
    }

    @Test
    void testEncryptConsistency() {
        String password = "testPassword123";
        String encrypted1 = MD5Util.encrypt(password);
        String encrypted2 = MD5Util.encrypt(password);

        assertEquals(encrypted1, encrypted2);
    }
}
