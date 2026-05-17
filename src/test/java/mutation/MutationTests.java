//package mutation;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//import com.pm.LuggageFareCalculator;
//import com.pm.LuggageFareCalculator.TicketClass;
//
//public class MutationTests {
//
//    private LuggageFareCalculator calculator;
//
//    // Targeting Mutant: changed conditional boundary in addLuggage (weight > ABSOLUTE_MAX_WEIGHT to weight >= ABSOLUTE_MAX_WEIGHT)
//    @Test
//    void testKillBoundaryMutant() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        // If the mutant changes > to >=, this will throw an exception incorrectly
//        assertDoesNotThrow(() -> calculator.addLuggage(32.0));
//    }
//
//    // Targeting Mutant: changed increment to decrement in freeBagsAllowed (freeBagsAllowed += 1 to freeBagsAllowed -= 1)
//    @Test
//    void testKillVipIncrementMutant() {
//        // Economy has 1 free bag. VIP adds 1, so 2 free bags total.
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
//        calculator.addLuggage(10.0);
//        calculator.addLuggage(10.0);
//
//        // Correct logic: 2 bags are free for VIP. Fee = 0.0.
//        // If mutant changes +=1 to something else, or removes it, fee will be > 0.
//        assertEquals(0.0, calculator.calculateTotalFee(), "VIP should have 2 free bags in Economy");
//    }
//
//    // Targeting Mutant: check if VIP gets Business weight limit (32.0) even in Economy
//    @Test
//    void testKillVipWeightLimitMutant() {
//        // Economy usually has 23.0 limit. VIP should have 32.0.
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
//        calculator.addLuggage(30.0);
//
//        // Correct logic: 30.0 is within 32.0 limit. Fee = 0.0.
//        // If mutant forgets to give VIP the Business limit, fee would be (30-23)*50 = 350.0.
//        assertEquals(0.0, calculator.calculateTotalFee(), "VIP should have 32kg limit even in Economy");
//    }
//
//    // Targeting Mutant: negated conditional in calculateTotalFee (weight > allowedWeight to weight <= allowedWeight)
//    @Test
//    void testKillWeightComparisonMutant() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(25.0); // 25 > 23 is True. Fee should be (25-23)*50 = 100.
//
//        double fee = calculator.calculateTotalFee();
//        assertEquals(100.0, fee);
//        // If the condition was inverted (weight <= allowedWeight), fee would be 0 for 25.0.
//    }
//
//    // Targeting Mutant: changed conditional boundary in luggageWeights.size() >= MAX_BAGS_PER_PASSENGER
//    @Test
//    void testKillMaxBagsBoundaryMutant() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        for(int i=0; i<5; i++) {
//            calculator.addLuggage(10.0);
//        }
//        // Should throw at 6th bag. If mutant is > instead of >=, it won't throw at 6th.
//        assertThrows(IllegalStateException.class, () -> calculator.addLuggage(10.0));
//    }
//}
