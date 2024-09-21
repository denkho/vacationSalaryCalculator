package ru.denkho.vacationsalarycalc.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Класс сервиса микросервиса для расчета отпускных
 * @author Denis Khokhlov
 * @version 1.0
 */
@Service
public class VacationCalcService {
    /** Поле - среднее количестве дней в месяце */
    public static final double AVERAGE_DAYS_IN_MONTH = 29.3;

    // Подсчет отпускных
    public double calculateVacationPayment (double averageSalary, int vacationDays) {
        double vacationPayment = (averageSalary / AVERAGE_DAYS_IN_MONTH) * vacationDays;
        return vacationPayment;
    }

    // Подсчет количества дней отпуска из переданных дат
    public Integer getNumberOfVacationDays(String vacationStartDate, String vacationEndDate){
        LocalDate date_start = LocalDate.parse(vacationStartDate);
        LocalDate date_end = LocalDate.parse(vacationEndDate);

        Integer vacationDays = (int) (date_end.toEpochDay() - date_start.toEpochDay()) + 1;
        return vacationDays;
    }
}
