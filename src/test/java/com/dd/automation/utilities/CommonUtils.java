package com.dd.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static final Logger LOGGER = LogManager.getLogger();

    public static String getValueInParanthesis(String arg1) {

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(arg1);
        String lastMatch = null;
        try {
            while (m.find()) {
                lastMatch = m.group(1);
            }
        } catch (Exception e) {
            CommonUtils.LOGGER.error("No value in Parenthesis", e);
        }
        return lastMatch;
    }

    public List<String> splitByDelimeter(String value, String del) {
        List<String> sample = new ArrayList<>();
        if (value.contains(del)) {
            sample = Arrays.asList(value.split(del));
        } else
            sample.add(value);

        return sample;
    }

    public String getPreviousDate(int field, int amount) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY");
        Calendar cal = Calendar.getInstance();
        cal.add(field, amount); // to get previous year add -1
        return dateFormat.format(cal.getTime());
    }

    public Boolean isDateInBetweenTwoDates(LocalDate validateDate, LocalDate currentDate, LocalDate previousDate){
        return (validateDate.isBefore(currentDate) && validateDate.isAfter(previousDate.minusDays(1)));
    }

    public LocalDate parseLocalDate(String arg, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(arg, formatter);
    }

    public LocalDate getToday(){
        return LocalDate.now();
    }

    public static int getRandomNumber(){
        Random rand = new Random();
        return rand.nextInt(9999);
    }
}
