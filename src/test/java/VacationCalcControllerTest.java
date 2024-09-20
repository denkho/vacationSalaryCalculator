import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.denkho.vacationsalarycalc.calculator.VacationCalcController;


/**
 * Класс для тестирования миросервиса рассчета отпускных
 * @author Denis Khokhlov
 * @version 1.0
 */
public class VacationCalcControllerTest {
    @ParameterizedTest
        @CsvSource({
                "50000, 14, null, null, 23890.784",
                "0, 0, null, null, 0",
                "50000, 0, null, null, 0",
                "-50000, 14, null, null, 0",
                "50000, 14, 2024-09-01, 2024-09-14, 23890.784",
                "0, 0, 2024-09-01, 2024-09-14, 0",
                "50000, 0, 2024-09-01, 2024-09-14, 0",
                "-50000, 14, 2024-09-01, 2024-09-14, 0",
        })
    public void calculateVacationPayTest(double averageSalary,
                                         int vacationDays,
                                         String vacationStartDate,
                                         String vacationEndDate,
                                         double expectedVacationPay
                                         ){
        VacationCalcController controller = new VacationCalcController();

        double actualVacationPay = controller.calculateVacationPayment(averageSalary, vacationDays, vacationStartDate, vacationEndDate);

        Assertions.assertEquals(expectedVacationPay, actualVacationPay, 0.001);
    }
}
