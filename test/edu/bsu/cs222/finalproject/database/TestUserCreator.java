package edu.bsu.cs222.finalproject.database;

import edu.bsu.cs222.finalproject.ui.UserCreator;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class TestUserCreator {

    @Test
    public void testIsPhoneNumberValid() {
        Method method = null;

        try {
            method = UserCreator.class.getDeclaredMethod("isPhoneNumberValid", String.class);
            method.setAccessible(true);

            Assert.assertEquals(0, method.invoke("1234567890"));
        } catch (Exception e) { //if any exception is thrown, fail the text, because, ya know, that doesn't fly
            Assert.fail();
        }
    }
}
