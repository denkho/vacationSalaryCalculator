package ru.denkho.vacationsalarycalc.calculator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс микросервиса для расчета отпускных
 * @author Denis Khokhlov
 * @version 1.0
 */

@RestController
public class VacationCalcController {
    /** Поле - среднее количестве дней в месяце */
    public static final double AVERAGE_DAYS_IN_MONTH = 29.3;

    /** Метод для расчета отпускных
     * @param averageSalary - средняя зарплата за предыдущие 12 месяцев
     * @param vacationDays - количество дней отпуска для расчета отпускных
     * @return возвращает сумму отпускных
     */
    @GetMapping("/calculate")
    public double calculateVacationPayment(@RequestParam double averageSalary,
                                           @RequestParam int vacationDays) {
        double vacationPayment;
        if (averageSalary < 0 || vacationDays < 0) {
            vacationPayment = 0;
        } else {
            vacationPayment = (averageSalary / AVERAGE_DAYS_IN_MONTH) * vacationDays;
        }
        return vacationPayment;
    }
}
