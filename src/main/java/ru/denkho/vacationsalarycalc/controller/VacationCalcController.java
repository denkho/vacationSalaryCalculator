package ru.denkho.vacationsalarycalc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.denkho.vacationsalarycalc.service.VacationCalcService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Класс контроллера микросервиса для расчета отпускных
 * @author Denis Khokhlov
 * @version 1.0
 */

@RestController
public class VacationCalcController {

    private final VacationCalcService vacationCalcService;

    public VacationCalcController(VacationCalcService vacationCalcService) {
        this.vacationCalcService = vacationCalcService;
    }

    /** Метод для расчета отпускных
     * @param averageSalary - средняя зарплата за предыдущие 12 месяцев
     * @param vacationDays - количество дней отпуска для расчета отпускных
     * @param vacationStartDate - принимает на вход дату первого дня отпуска в формате ГГГГ-ММ-ДД
     * @param vacationEndDate - принимает на вход дату последнего дня отпуска в формате ГГГГ-ММ-ДД
     * @return возвращает сумму отпускных
     */
    @GetMapping("/calculate")
    public double calculateVacationPayment(@RequestParam double averageSalary,
                                           @RequestParam(required = false) Integer vacationDays,
                                           @RequestParam(required = false) String vacationStartDate,
                                           @RequestParam(required = false) String vacationEndDate) {


        // Проверка корректности данных о дате отпуска и/или количестве дней отпуска
        if (vacationDays == null) {
            if (vacationStartDate == null || vacationEndDate == null) {
                throw new IllegalArgumentException("Обе даты начала и конца отпуска должны быть указаны.");

            }

            try {

                vacationDays = vacationCalcService.getNumberOfVacationDays(vacationStartDate, vacationEndDate);

            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты. Используйте формат ГГГГ-ММ-ДД.");
            }
        }

        // Проверка отрицательных значений  средней зарплаты и дней отпуска
        if (averageSalary < 0 || vacationDays < 0) {
            throw new IllegalArgumentException("Значения зарплаты или дней отпуска не могут быть отрицательными.");
        }

        return vacationCalcService.calculateVacationPayment(averageSalary, vacationDays);

    }
}
