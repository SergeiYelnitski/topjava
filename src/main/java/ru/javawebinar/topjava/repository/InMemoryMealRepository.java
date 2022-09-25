package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 28, 10, 0), "Завтрак", 500));
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 28, 13, 0), "Обед", 1000));
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 28, 20, 0), "Ужин", 500));
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 29, 10, 0), "Завтрак", 1000));
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 29, 14, 0), "Обед", 500));
        save(new Meal(LocalDateTime.of(2022, Month.SEPTEMBER, 29, 21, 0), "Ужин", 510));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        return repository.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}
