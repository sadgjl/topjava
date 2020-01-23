package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        List<UserMeal> userMeal = new ArrayList<>();
        Map<LocalDate, Integer> eachDayMeal = new HashMap<>();
        if (!meals.isEmpty()) {

            //Creating a Map for each day with calories
            for (UserMeal usermeal: meals) {
                eachDayMeal.put(usermeal.getDateTime().toLocalDate(), eachDayMeal.getOrDefault(usermeal.getDateTime().toLocalDate(), 0) + usermeal.getCalories());
            }

            //filtering list for specific "from-to" time with cycle
            for (UserMeal userMeal1: meals) {
                if (userMeal1.getDateTime().toLocalTime().toSecondOfDay() >= startTime.toSecondOfDay() &&
                userMeal1.getDateTime().toLocalTime().toSecondOfDay() <= endTime.toSecondOfDay()) {
                    userMeal.add(userMeal1);
                }
            }

            //Adding filtered list to new List with excess condition
            for (UserMeal userMealL: userMeal
                 ) {
                userMealWithExcesses.add(new UserMealWithExcess(userMealL.getDateTime(), userMealL.getDescription(), userMealL.getCalories(), eachDayMeal.get(userMealL.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        if (userMealWithExcesses.isEmpty()) {
            System.out.println("Sorry, don't eat at this time ;)");
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        List<UserMeal> userMeal = new ArrayList<>();
        Map<LocalDate, Integer> eachDayMeal = new HashMap<>();
        if (!meals.isEmpty()) {

            //Creating a Map for each day with calories
            for (UserMeal usermeal: meals) {
                eachDayMeal.put(usermeal.getDateTime().toLocalDate(), eachDayMeal.getOrDefault(usermeal.getDateTime().toLocalDate(), 0) + usermeal.getCalories());
            }

            //filtering list for specific "from-to" time
            userMeal = meals.stream().filter(c -> (c.getDateTime().toLocalTime().toSecondOfDay() >= startTime.toSecondOfDay() && c.getDateTime().toLocalTime().toSecondOfDay() <= endTime.toSecondOfDay())).collect(Collectors.toList());

            //userMeal.forEach(System.out::println);

            //Adding filtered list to new List with excess condition
            for (UserMeal userMealL: userMeal
            ) {
                userMealWithExcesses.add(new UserMealWithExcess(userMealL.getDateTime(), userMealL.getDescription(), userMealL.getCalories(), eachDayMeal.get(userMealL.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        if (userMealWithExcesses.isEmpty()) {
            System.out.println("Sorry, don't eat at this time ;)");
        }
        return userMealWithExcesses;
    }
}
