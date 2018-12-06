package edu.bsu.cs222.finalproject.backend;

import org.junit.Test;

import static org.junit.Assert.*;

public class CurrencyTest {

    @Test
    public void isValid() {
        assertTrue(Currency.isValid("12.34"));
        assertTrue(Currency.isValid("12.3"));
        assertTrue(Currency.isValid("12.00"));
        assertTrue(Currency.isValid("12."));
        assertTrue(Currency.isValid("12."));
        assertTrue(Currency.isValid("12"));
        assertTrue(Currency.isValid("1 2"));
        assertTrue(Currency.isValid("$12.34"));
        assertTrue(Currency.isValid(" 12.34 "));
        assertTrue(Currency.isValid(" $12.34 "));
        assertTrue(Currency.isValid("0"));

        assertFalse(Currency.isValid(""));
        assertFalse(Currency.isValid("-12.34"));
        assertFalse(Currency.isValid("text"));
        assertFalse(Currency.isValid("$12text.34"));
        assertFalse(Currency.isValid("12.000"));
        assertFalse(Currency.isValid(".0000"));
    }

    @Test
    public void toNormalized() {
        assertEquals(1234, Currency.toNormalized("12.34"));
        assertEquals(1230, Currency.toNormalized("12.30"));
        assertEquals(1200, Currency.toNormalized("12."));
        assertEquals(30, Currency.toNormalized("0.30"));
        assertEquals(1230, Currency.toNormalized("12.3"));
        assertEquals(1200, Currency.toNormalized("12"));
        assertEquals(1200, Currency.toNormalized("1 2"));
        assertEquals(1234, Currency.toNormalized("$12.34"));
        assertEquals(1234, Currency.toNormalized(" 12.34 "));
        assertEquals(1200, Currency.toNormalized(" $12 "));
    }

    @Test
    public void toFormatted() {
        assertEquals("$12.34", Currency.toFormatted(1234));
        assertEquals("$12.30", Currency.toFormatted(1230));
        assertEquals("$12.00", Currency.toFormatted(1200));
        assertEquals("$0.30", Currency.toFormatted(30));
        assertEquals("$0.00", Currency.toFormatted(0));
    }
}