package edu.bsu.cs222.finalproject.backend;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneNumberTest {

    @Test
    public void testIsValid() {
        assertEquals(true, PhoneNumber.isValid("5555555555"));
        assertEquals(true, PhoneNumber.isValid("555-555-5555"));
        assertEquals(true, PhoneNumber.isValid("(555)-555-5555"));

        assertEquals(false, PhoneNumber.isValid("555555555"));
        assertEquals(false, PhoneNumber.isValid("55555555555"));
        assertEquals(false, PhoneNumber.isValid("555-5555-5555"));
        assertEquals(false, PhoneNumber.isValid("(555)-5555-5555"));
        assertEquals(false, PhoneNumber.isValid("555-555-555"));
        assertEquals(false, PhoneNumber.isValid("555-555-five"));
        assertEquals(false, PhoneNumber.isValid("(555)-555-5555what"));
    }

    @Test
    public void testToNormalized() throws Exception{
        assertEquals("5555555555", PhoneNumber.toNormalized("5555555555"));
        assertEquals("5555555555", PhoneNumber.toNormalized("555-555-5555"));
        assertEquals("5555555555", PhoneNumber.toNormalized("(555)-555-5555"));
        assertEquals("5555555555", PhoneNumber.toNormalized("5-5-5-5-5-55-5-55"));
    }

    @Test
    public void testToFormatted() throws Exception{
        assertEquals("555-555-5555", PhoneNumber.toFormatted("5555555555"));
        assertEquals("123-456-7890", PhoneNumber.toFormatted("1234567890"));
    }
}