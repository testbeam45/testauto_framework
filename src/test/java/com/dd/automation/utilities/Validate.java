package com.dd.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;

public class Validate {
    private static final Logger LOGGER = LogManager.getLogger();

    public <T> void isNull(final T actual) {
        try {
            assertThat(actual).isNull();
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void equals(final T actual, final T expected) {
        try {
            if (actual instanceof String && expected instanceof String)
                assertThat(actual).isEqualTo(expected);
            else
                assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void notEquals(final T actual, final T expected) {
        try {
            assertThat(actual).isNotEqualTo(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T, U> void inMap(final Map<T, U> actual, final Map<T, U> expected) {
        try {
            Map<String, String> newActual = actual.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue() == null ? "null" : entry.getValue().toString()));
            Map<String, String> newExpected = expected.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> entry.getValue() == null ? "null" : entry.getValue().toString()));
            assertThat(newActual).containsAllEntriesOf(newExpected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void containsOnly(final Iterable<T> actual, final T... expected) {
        try {
            assertThat(actual).containsOnly(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void contains(final Iterable<T> actual, final T expected) {
        try {
            if (expected instanceof String)
                assertThat(actual).contains(expected);
            else
                assertThat(actual).usingRecursiveFieldByFieldElementComparator().contains(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void notContains(final Iterable<T> actual, final T expected) {
        try {
            assertThat(actual).usingRecursiveFieldByFieldElementComparator().doesNotContain(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void notContains(String actual, String expected) {
        try {
            assertThat(actual).doesNotContain(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void containsItemsOf(final Iterable<T> actual, final Iterable<T> expected) {
        try {
            assertThat(actual).usingRecursiveFieldByFieldElementComparator().containsAll(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T> void notContainItemsOf(final Iterable<T> actual, final Iterable<T> expected) {
        try {
            assertThat(actual).usingRecursiveFieldByFieldElementComparator().doesNotContainAnyElementsOf(expected);
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T extends Comparable<T>> void sortedAsc(final List<T> actual) {
        try {
            assertThat(actual).isSorted();
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public <T extends Comparable<T>> void sortedDsc(final List<T> actual) {
        try {
            assertThat(actual).isSortedAccordingTo(reverseOrder());
        } catch (AssertionError e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public void isDateOfPattern(String format, String date) {
        Date myDate;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        try {
            myDate = formatter.parse(date);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw newError(format("%s is not of %s format.", date, format));
        }
        equals(myDate != null, Boolean.TRUE);
    }

    public void isDateInBetweenTwoDates(LocalDate validateDate, LocalDate currentDate, LocalDate previousDate){
        try {
            Assert.assertTrue(validateDate.isBefore(currentDate) && validateDate.isAfter(previousDate.minusDays(1)));
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw newError(format("%s date is not within the range", validateDate));
        }
    }

//    public static void isPastDate(String format, String date) {
//        equals(isAPastDate(format, date), Boolean.TRUE);
//    }
//
//    public static void isNotPastDate(String format, String date) {
//        equals(isAPastDate(format, date), Boolean.FALSE);
//    }

    public AssertionError newError(String message) {
        return new AssertionError(message);
    }
}
