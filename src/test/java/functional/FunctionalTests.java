//package functional;
//
//import com.pm.LuggageFareCalculator;
//import com.pm.LuggageFareCalculator.TicketClass;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FunctionalTests {
//
//    private LuggageFareCalculator calculator;
//
//    // Oracle method to verify expected results
//    private double calculateExpectedFee(TicketClass ticketClass, boolean isVip, double[] weights) {
//        if (weights.length == 0) return 0.0;
//
//        double totalFee = 0.0;
//        double allowedWeight = (ticketClass == TicketClass.BUSINESS || isVip) ? 32.0 : 23.0;
//        int freeBagsAllowed = (ticketClass == TicketClass.BUSINESS) ? 2 : 1;
//        if (isVip) freeBagsAllowed += 1;
//
//        for (int i = 0; i < weights.length; i++) {
//            double weight = weights[i];
//            boolean isExtraBag = i >= freeBagsAllowed;
//
//            if (isExtraBag) {
//                totalFee += 100.0;
//            }
//            if (weight > allowedWeight) {
//                totalFee += (weight - allowedWeight) * 50.0;
//            }
//        }
//        return totalFee;
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideEquivalencePartitioningCases")
//    void testEquivalencePartitioning(TicketClass ticketClass, boolean isVip, double[] weights) {
//        calculator = new LuggageFareCalculator(ticketClass, isVip);
//        for (double w : weights) {
//            calculator.addLuggage(w);
//        }
//        assertEquals(calculateExpectedFee(ticketClass, isVip, weights), calculator.calculateTotalFee(), 0.001);
//    }
//
//    private static Stream<Arguments> provideEquivalencePartitioningCases() {
//        return Stream.of(
//            // Economy, non-VIP, 1 bag (free), within weight
//            Arguments.of(TicketClass.ECONOMY, false, new double[]{20.0}),
//            // Economy, non-VIP, 1 bag, overweight
//            Arguments.of(TicketClass.ECONOMY, false, new double[]{25.0}),
//            // Economy, non-VIP, 2 bags, within weight
//            Arguments.of(TicketClass.ECONOMY, false, new double[]{20.0, 20.0}),
//            // Business, non-VIP, 2 bags (free), within weight
//            Arguments.of(TicketClass.BUSINESS, false, new double[]{30.0, 30.0}),
//            // Business, non-VIP, 3 bags, overweight
//            Arguments.of(TicketClass.BUSINESS, false, new double[]{32.0, 31.0, 31.0}), // Note: 32.0 is the max allowed weight
//            // VIP, regardless of class/weight
//            Arguments.of(TicketClass.ECONOMY, true, new double[]{32.0, 32.0, 32.0})
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideBVACases")
//    void testBoundaryValueAnalysis(TicketClass ticketClass, boolean isVip, double weight, double expectedFee) {
//        calculator = new LuggageFareCalculator(ticketClass, isVip);
//        calculator.addLuggage(weight);
//        assertEquals(expectedFee, calculator.calculateTotalFee(), 0.001);
//    }
//
//    private static Stream<Arguments> provideBVACases() {
//        return Stream.of(
//            // Boundary for Economy weight: 23.0
//            Arguments.of(TicketClass.ECONOMY, false, 23.0, 0.0),
//            Arguments.of(TicketClass.ECONOMY, false, 23.1, 0.1 * 50.0),
//            Arguments.of(TicketClass.ECONOMY, false, 22.9, 0.0),
//
//            // Boundary for Business weight: 32.0
//            Arguments.of(TicketClass.BUSINESS, false, 32.0, 0.0),
//            Arguments.of(TicketClass.BUSINESS, false, 31.9, 0.0),
//
//            // Boundary for weight input: 0.1 and 32.0
//            Arguments.of(TicketClass.ECONOMY, false, 0.1, 0.0),
//            Arguments.of(TicketClass.ECONOMY, false, 32.0, (32.0-23.0)*50.0)
//        );
//    }
//
//    @Test
//    void testInvalidWeightsBVA() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        assertThrows(IllegalArgumentException.class, () -> calculator.addLuggage(0.0));
//        assertThrows(IllegalArgumentException.class, () -> calculator.addLuggage(32.1));
//        assertThrows(IllegalArgumentException.class, () -> calculator.addLuggage(-1.0));
//    }
//
//    @Test
//    void testMaxBagsBVA() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        for (int i = 0; i < 5; i++) {
//            calculator.addLuggage(10.0);
//        }
//        assertThrows(IllegalStateException.class, () -> calculator.addLuggage(10.0));
//    }
//}
