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

    @Test
    @DisplayName("BVA: Limita numarului maxim de bagaje (MAX_BAGS_PER_PASSENGER = 5)")
    void testMaxBagsBoundary() {
        for (int i = 0; i < 5; i++) {
            economyNormalCalc.addLuggage(10.0);
        }
        assertEquals(5, economyNormalCalc.getLuggageCount());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            economyNormalCalc.addLuggage(15.0);
        });
        assertTrue(exception.getMessage().contains("S-a atins numarul maxim de bagaje permise"));
    }

    @Test
    @DisplayName("Condition/Decision: True && True (Greutate depasita, nu este VIP)")
    void testConditionWeightExceededNotVip() {
        economyNormalCalc.addLuggage(24.0);
        double expectedFee = 1.0 * LuggageFareCalculator.OVERWEIGHT_FEE_PER_KG;
        assertEquals(expectedFee, economyNormalCalc.calculateTotalFee());
    }

    @Test
    @DisplayName("Condition/Decision: True && False (Greutate depasita, DAR este VIP)")
    void testConditionWeightExceededIsVip() {
        LuggageFareCalculator vipEconomy = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.ECONOMY, true);
        vipEconomy.addLuggage(24.0);
        assertEquals(0.0, vipEconomy.calculateTotalFee(), "VIP-urile nu platesc taxa de supragreutate");
    }

    @Test
    @DisplayName("Condition/Decision: False && True (Greutate normala, nu este VIP)")
    void testConditionWeightNormalNotVip() {
        economyNormalCalc.addLuggage(20.0);
        assertEquals(0.0, economyNormalCalc.calculateTotalFee());
    }

    @Test
    @DisplayName("McCabe Path 1: Fara bagaje (Iesire rapida din metoda)")
    void testEmptyLuggagePath() {
        assertEquals(0.0, economyNormalCalc.calculateTotalFee(), "Taxa pentru 0 bagaje trebuie sa fie 0");
    }

    @Test
    @DisplayName("McCabe Path 2 & Statement Coverage: Resetarea starii bagajelor")
    void testResetLuggage() {
        economyNormalCalc.addLuggage(15.0);
        assertEquals(1, economyNormalCalc.getLuggageCount());

        economyNormalCalc.resetLuggage();
        assertEquals(0, economyNormalCalc.getLuggageCount());
        assertEquals(0.0, economyNormalCalc.calculateTotalFee());
    }

    @Test
    @DisplayName("McCabe Path 3: Bagaje suplimentare pentru Business + VIP")
    void testBusinessVipExtraBags() {
        businessVipCalc.addLuggage(10.0);
        businessVipCalc.addLuggage(10.0);
        businessVipCalc.addLuggage(10.0);
        businessVipCalc.addLuggage(10.0);

        assertEquals(0.0, businessVipCalc.calculateTotalFee(), "VIP-urile nu sunt taxate nici pentru bagaj extra conform implementarii");
    }

    @Test
    @DisplayName("McCabe Path 4: Combinatie complexa (Bagaj extra + Supragreutate + Economy)")
    void testComplexScenario() {
        economyNormalCalc.addLuggage(25.0);
        economyNormalCalc.addLuggage(20.0);

        double expectedFee = (2.0 * LuggageFareCalculator.OVERWEIGHT_FEE_PER_KG) + LuggageFareCalculator.EXTRA_BAG_FEE;
        assertEquals(expectedFee, economyNormalCalc.calculateTotalFee());
    }

    @Test
    @DisplayName("Kill Mutant 1: Schimbarea <= cu < in validarea greutatii")
    void testKillWeightZeroMutant() {
        assertThrows(IllegalArgumentException.class, () -> {
            economyNormalCalc.addLuggage(0.0);
        }, "Greutatea de exact 0.0 trebuie sa arunce exceptie");
    }

    @Test
    @DisplayName("Kill Mutant 2: Modificarea adunarii += in -= pentru bagajele gratuite ale VIP-urilor")
    void testKillVipFreeBagsMutant() {
        LuggageFareCalculator vipEconomy = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.ECONOMY, true);
        vipEconomy.addLuggage(10.0);
        vipEconomy.addLuggage(10.0);

        assertEquals(0.0, vipEconomy.calculateTotalFee(),
                "VIP pe economy trebuie sa aiba 2 bagaje gratuite. Mutantul care scade bagajele va pica acest test");
    }

}