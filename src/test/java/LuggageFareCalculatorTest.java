import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class LuggageFareCalculatorTest {

    private LuggageFareCalculator economyNormalCalc;
    private LuggageFareCalculator businessVipCalc;

    @BeforeEach
    void setUp() {
        economyNormalCalc = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.ECONOMY, false);
        businessVipCalc = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.BUSINESS, true);
    }

    @Test
    @DisplayName("EP: Constructorul arunca exceptie pentru clasa de zbor null")
    void testConstructorNullClass() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new LuggageFareCalculator(null, false);
        });
        assertEquals("Clasa de zbor nu poate fi null", exception.getMessage());
    }

    @Test
    @DisplayName("EP: Calcul taxa pentru bagaj valid, sub limita (0 taxa)")
    void testValidLuggageNoFee() {
        economyNormalCalc.addLuggage(20.0);
        assertEquals(0.0, economyNormalCalc.calculateTotalFee(),
                "Nu ar trebui sa existe taxe pentru un bagaj sub limita de Economy");
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, true",
            "0.01, false",
            "32.0, false",
            "32.01, true"
    })
    @DisplayName("BVA: Testarea limitelor de greutate per bagaj (0.0 si 32.0)")
    void testBoundaryLuggageWeights(double weight, boolean shouldThrowException) {
        if (shouldThrowException) {
            assertThrows(IllegalArgumentException.class, () -> economyNormalCalc.addLuggage(weight));
        } else {
            assertDoesNotThrow(() -> economyNormalCalc.addLuggage(weight));
        }
    }

}