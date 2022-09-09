package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDateTime, Integer> mapByLocalTime = new HashMap<>();

        for (UserMeal userMeal : meals) {
            LocalDateTime time = userMeal.getDateTime();
            if (!mapByLocalTime.containsKey(time)) {
                mapByLocalTime.put(time, userMeal.getCalories());
            } else {
                mapByLocalTime.merge(time, userMeal.getCalories(), Integer::sum);
            }
        }

        List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();

        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(
                        new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                                mapByLocalTime.get(userMeal.getDateTime()) > caloriesPerDay
                        ));
            }
        }

        return mealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalTime, Integer> caloriesBySum =
                meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalTime(),
                        Collectors.summingInt(UserMeal::getCalories)));



        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(),
                        caloriesBySum.get(userMeal.getDateTime().toLocalTime()) > caloriesPerDay))
                .collect(Collectors.toList());


    }
}
