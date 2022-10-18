package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);


    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        final int userId = SecurityUtil.authUserId();
        log.info("get all meals for user id {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        final int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return service.get(userId, id);
    }

    public void delete(int id) {
        final int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user{}", id, userId);
        service.delete(userId, id);
    }

    public Meal create(Meal meal) {
        final int userId = SecurityUtil.authUserId();
        ValidationUtil.checkNew(meal);
        log.info("create meal for user {}", userId);
        return service.create(userId, meal);
    }

    public void update(int id, Meal meal) {
        final int userId = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(meal, id);
        log.info("update meal {} for user {}", id, userId);
        service.update(userId, meal);
    }

    public List<MealTo> getAllFilterBetweenDateAndTime(LocalDate startDate, LocalDate endDate,
                                                       LocalTime startTime, LocalTime endTime) {
        final int userId = SecurityUtil.authUserId();
        log.info("filter between date({} - {}) time ({} - {}) for user {}",
                startDate, endDate, startTime, endDate, userId);
        List<Meal> filterMealsBetweenDate = service.getBetweenDateInclusive(userId, startDate, endDate);
        return MealsUtil.getFilteredTos(filterMealsBetweenDate, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
        
    }
}