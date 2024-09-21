package ru.denkho.vacationsalarycalc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

/**
 * Класс для тестирования миросервиса рассчета отпускных
 * @author Denis Khokhlov
 * @version 1.0
 */
@WebMvcTest(VacationCalcControllerTest.class)
public class VacationCalcControllerTest {
    @Autowired MockMvc mvc;

    @MockBean
    VacationCalcController vacationCalcController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Позитивный тест - ввод корректных данных о зарплате и количестве дней отпуска
     */
    @Test
    public void calculateVacationPaymentShouldReturnCorrectAmount() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                14,
                null,
                null))
                .thenReturn(28668.94);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("averageSalary", "60000")
                .param("vacationDays", "14"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("28668.94"));
    }

    /**
     * Позитивный тест - ввод корректных данных о зарплате, дат начала и конца отпуска
     */
    @Test
    public void calculateVacationPaymentShouldReturnCorrectAmountWhenDateStartAndEndStated() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                null,
                "2024-09-01",
                "2024-09-14"))
                .thenReturn(28668.94);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "60000")
                        .param("vacationStartDate", "2024-09-01")
                        .param("vacationEndDate", "2024-09-14"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("28668.94"));
    }

    /**
     * Тестирование граничных значений - передается продолжительность отпуска в один день
     */
    @Test
    public void calculateVacationPaymentShouldHandleOneDayVacation() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                1,
                null,
                null))
                .thenReturn(2047.78);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "60000")
                        .param("vacationDays", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("2047.78")); // Example result for 1-day vacation
    }

    /**
     * Тестирование граничных значений - проверка расчета отпуска при старте отпуска в одном месяце
     * и его окончании в другом. В параметрах передаются даты начала и окончания отпуска.
     */
    @Test
    public void calculateVacationPaymentShouldHandleVacationAcrossTwoMonths() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                null,
                "2024-01-30",
                "2024-02-05"))
                .thenReturn(14334.47);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "60000")
                        .param("vacationStartDate", "2024-01-30")
                        .param("vacationEndDate", "2024-02-05"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("14334.47")); // Example expected output
    }

    /**
     * Тест - выдается ошибка при передаче неверной даты
     */
    @Test
    public void calculateVacationPaymentShouldReturnBadRequestWhenInvalidDate() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                null,
                "some_date",
                null))
                .thenThrow(IllegalArgumentException.class);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "60000")
                        .param("vacationStartDate", "some_date"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Тест - выдается ошибка при получении отрицательного значения в количестве дней отпуска
     */
    @Test
    public void calculateVacationPayment_ShouldReturnBadRequest_WhenNegativeDays() throws Exception {
        when(vacationCalcController.calculateVacationPayment(
                60000,
                -5,
                null,
                null))
                .thenThrow(IllegalArgumentException.class);

        mvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "60000")
                        .param("vacationDays", "-5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
