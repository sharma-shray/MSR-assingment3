/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freenet.support;

import static java.util.Calendar.MILLISECOND;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test case for {@link freenet.support.TimeUtil} class.
 *
 * @author Alberto Bacchelli &lt;sback@freenetproject.org&gt;
 */
public class TimeUtilTest extends TestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //1w+1d+1h+1m+1s+1ms
    private long oneForTermLong = 694861001;

    @Override
    protected void setUp() throws Exception {
        Locale.setDefault(Locale.US);
    }

    /**
     * Tests formatTime(long,int,boolean) method
     * trying the biggest long value
     */
    public void testFormatTime_LongIntBoolean_MaxValue() {
        String expectedForMaxLongValue = "15250284452w3d7h12m55.807s";
        assertEquals(TimeUtil.formatTime(Long.MAX_VALUE, 6, true),
                expectedForMaxLongValue);
    }

    /**
     * Tests formatTime(long,int) method
     * trying the biggest long value
     */
    public void testFormatTime_LongInt() {
        String expectedForMaxLongValue = "15250284452w3d7h12m55s";
        assertEquals(TimeUtil.formatTime(Long.MAX_VALUE, 6),
                expectedForMaxLongValue);
    }

    /**
     * Tests formatTime(long) method
     * trying the biggest long value
     */
    public void testFormatTime_Long() {
        //it uses two terms by default
        String expectedForMaxLongValue = "15250284452w3d";
        assertEquals(TimeUtil.formatTime(Long.MAX_VALUE),
                expectedForMaxLongValue);
    }

    @Test
    public void testFormatTime() {
        assertEquals("0s", TimeUtil.formatTime(10L));
        assertEquals("1s", TimeUtil.formatTime(1000L));
        assertEquals("0s", TimeUtil.formatTime(-1L));
        assertEquals("15250284452w3d", TimeUtil.formatTime(9223372036854775807L));
        assertEquals("0s", TimeUtil.formatTime(10L, 3));
        assertEquals("1s", TimeUtil.formatTime(1000L, 3));
        assertEquals("0s", TimeUtil.formatTime(-1L, 3));
        assertEquals("15250284452w3d7h", TimeUtil.formatTime(9223372036854775807L, 3));
        assertEquals("", TimeUtil.formatTime(1000L, 0));
        assertEquals("15250284452w3d7h12m55s", TimeUtil.formatTime(9223372036854775807L, 6));
        assertEquals("15250284452w", TimeUtil.formatTime(9223372036854775807L, 1));
        assertEquals("15250284452w3d", TimeUtil.formatTime(9223372036854775807L, 2));
        assertEquals("0.010s", TimeUtil.formatTime(10L, 3, true));
        assertEquals("", TimeUtil.formatTime(0L, 3, true));
        assertEquals("-0.001s", TimeUtil.formatTime(-1L, 3, true));
        assertEquals("15250284452w3d7h", TimeUtil.formatTime(9223372036854775807L, 3, true));
        assertEquals("", TimeUtil.formatTime(10L, 0, true));
        assertEquals("", TimeUtil.formatTime(10L, 1, true));
        assertEquals("0s", TimeUtil.formatTime(10L, 3, false));
        assertEquals("15250284452w3d7h12m55.807s", TimeUtil.formatTime(9223372036854775807L, 6, true));
        assertEquals("15250284452w", TimeUtil.formatTime(9223372036854775807L, 1, true));
        assertEquals("15250284452w3d", TimeUtil.formatTime(9223372036854775807L, 2, true));
        assertEquals("15250284452w3d7h", TimeUtil.formatTime(9223372036854775807L, 3, false));
        assertEquals("1s", TimeUtil.formatTime(1000L, 3, false));
    }

    @Test
    public void testFormatTime2() {
        thrown.expect(IllegalArgumentException.class);
        TimeUtil.formatTime(10L, 64);
    }

    @Test
    public void testFormatTime3() {
        thrown.expect(IllegalArgumentException.class);
        TimeUtil.formatTime(10L, 64, true);
    }

    /**
     * Tests formatTime(long) method
     * using known values.
     * They could be checked using Google Calculator
     * http://www.google.com/intl/en/help/features.html#calculator
     */
    public void testFormatTime_KnownValues() {
        Long methodLong;
        String[][] valAndExpected = {
                //one week
                {"604800000", "1w"},
                //one day
                {"86400000", "1d"},
                //one hour
                {"3600000", "1h"},
                //one minute
                {"60000", "1m"},
                //one second
                {"1000", "1s"}
        };
        for (int i = 0; i < valAndExpected.length; i++) {
            methodLong = Long.valueOf(valAndExpected[i][0]);
            assertEquals(TimeUtil.formatTime(methodLong.longValue()),
                    valAndExpected[i][1]);
        }
    }

    /**
     * Tests formatTime(long,int) method
     * using a long value that generate every possible
     * term kind. It tests if the maxTerms arguments
     * works correctly
     */
    public void testFormatTime_LongIntBoolean_maxTerms() {
        String[] valAndExpected = {
                //0 terms
                "",
                //1 term
                "1w",
                //2 terms
                "1w1d",
                //3 terms
                "1w1d1h",
                //4 terms
                "1w1d1h1m",
                //5 terms
                "1w1d1h1m1s",
                //6 terms
                "1w1d1h1m1.001s"
        };
        for (int i = 0; i < valAndExpected.length; i++)
            assertEquals(TimeUtil.formatTime(oneForTermLong, i, true),
                    valAndExpected[i]);
    }

    /**
     * Tests formatTime(long,int) method
     * using one millisecond time interval.
     * It tests if the withSecondFractions argument
     * works correctly
     */
    public void testFormatTime_LongIntBoolean_milliseconds() {
        long methodValue = 1;    //1ms
        assertEquals(TimeUtil.formatTime(methodValue, 6, false), "0s");
        assertEquals(TimeUtil.formatTime(methodValue, 6, true), "0.001s");
    }

    /**
     * Tests formatTime(long,int) method
     * using a long value that generate every possible
     * term kind. It tests if the maxTerms arguments
     * works correctly
     */
    public void testFormatTime_LongIntBoolean_tooManyTerms() {
        try {
            TimeUtil.formatTime(oneForTermLong, 7);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException anException) {
            assertNotNull(anException);
        }
    }

    /**
     * Tests {@link TimeUtil#setTimeToZero(Date)}
     */
    public void testSetTimeToZero() {
        // Test whether zeroing doesn't happen when it needs not to.

        GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.set(2015, 0 /* 0-based! */, 01, 00, 00, 00);
        c.set(MILLISECOND, 0);

        Date original = c.getTime();
        Date zeroed = TimeUtil.setTimeToZero(original);

        assertEquals(original, zeroed);
        // Date objects are mutable so their recycling is discouraged, check for it
        assertNotSame(original, zeroed);

        // Test whether zeroing happens when it should.

        c.set(2014, 12 - 1 /* 0-based! */, 31, 23, 59, 59);
        c.set(MILLISECOND, 999);
        original = c.getTime();
        Date originalBackup = (Date) original.clone();

        c.set(2014, 12 - 1 /* 0-based! */, 31, 00, 00, 00);
        c.set(MILLISECOND, 0);
        Date expected = c.getTime();

        zeroed = TimeUtil.setTimeToZero(original);

        assertEquals(expected, zeroed);
        assertNotSame(original, zeroed);
        // Check for bogus tampering with original object
        assertEquals(originalBackup, original);
    }

    @Test
    public void testSetTimeToZero2() {
        // TODO: This test is incomplete.
        //   Reason: No meaningful assertions found.
        //   To help Diffblue Cover to find assertions, please add getters to the
        //   class under test that return fields written by the method under test.
        //   See https://diff.blue/R004

        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        TimeUtil.setTimeToZero(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public void testToMillis_oneForTermLong() {
        assertEquals(TimeUtil.toMillis("1w1d1h1m1.001s"), oneForTermLong);
    }

    public void testToMillis_maxLong() {
        assertEquals(TimeUtil.toMillis("15250284452w3d7h12m55.807s"), Long.MAX_VALUE);
    }

    public void testToMillis_minLong() {
        assertEquals(TimeUtil.toMillis("-15250284452w3d7h12m55.808s"), Long.MIN_VALUE);
    }

    @Test
    public void testToMillis() {
        thrown.expect(NumberFormatException.class);
        TimeUtil.toMillis("Time Interval");
    }

    @Test
    public void testToMillis2() {
        assertEquals(0L, TimeUtil.toMillis("-"));
    }

    @Test
    public void testToMillis3() {
        thrown.expect(NumberFormatException.class);
        TimeUtil.toMillis("(?<=[a-z])");
    }

    @Test
    public void testToMillis4() {
        assertEquals(0L, TimeUtil.toMillis("0s"));
    }

    public void testToMillis_empty() {
        assertEquals(TimeUtil.toMillis(""), 0);
        assertEquals(TimeUtil.toMillis("-"), 0);
    }

    public void testToMillis_unknownFormat() {
        try {
            TimeUtil.toMillis("15250284452w3q7h12m55.807s");
        } catch (NumberFormatException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testMakeHTTPDate() {
        assertEquals("Thu, 01 Jan 1970 00:00:00 GMT", TimeUtil.makeHTTPDate(10L));
    }
}
