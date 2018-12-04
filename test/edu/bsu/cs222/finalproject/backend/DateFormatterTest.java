package edu.bsu.cs222.finalproject.backend;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;

public class DateFormatterTest {

    @Test
    public void formatDate() {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);

        cal.set(2018, 2, 24, 3, 20, 0);//year, month(January is 0), day, hour, minute, second
        Assert.assertEquals("2018-03-24 03:20AM", DateFormatter.formatDate(new Date(cal.getTime().getTime())));

        cal.set(2018, 2, 24, 13, 20, 18);
        Assert.assertEquals("2018-03-24 01:20PM", DateFormatter.formatDate(new Date(cal.getTime().getTime())));

        cal.set(1876, 11, 31, 13, 20, 18);
        Assert.assertEquals("1876-12-31 01:20PM", DateFormatter.formatDate(new Date(cal.getTime().getTime())));
    }
}