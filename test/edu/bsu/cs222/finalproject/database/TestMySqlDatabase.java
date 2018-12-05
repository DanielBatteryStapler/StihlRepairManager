package edu.bsu.cs222.finalproject.database;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestMySqlDatabase {
    @Test
    public void testEscapeForLike() {
        assertEquals("hello", MySqlDatabase.escapeForLike("hello"));
        assertEquals("hel!!lo", MySqlDatabase.escapeForLike("hel!lo"));
        assertEquals("hel!%lo", MySqlDatabase.escapeForLike("hel%lo"));
        assertEquals("he!!ll!%o", MySqlDatabase.escapeForLike("he!ll%o"));
        assertEquals("hel![lo", MySqlDatabase.escapeForLike("hel[lo"));
    }
}
