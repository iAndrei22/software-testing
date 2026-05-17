//package structural;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//import com.pm.LuggageFareCalculator;
//import com.pm.LuggageFareCalculator.TicketClass;
//
//
//public class StructuralTests {
//
//    private LuggageFareCalculator calculator;
//
//    /// ! - Daca comentam testStatementCoverage si testDecisionCoverage coverage-ul nu se modifica.
//    /// Pentru ca in testarea whitebox: McCabe le acopera.
//
//    // Statement Coverage: Ensuring every line of code is executed
////    @Test
////    void testStatementCoverage() {
////        // Path to trigger luggageWeights.isEmpty()
////        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
////        assertEquals(0.0, calculator.calculateTotalFee());
////
////        // Path to trigger loop and all internal IFs
////        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
////        calculator.addLuggage(25.0); // Overweight (Decision 7)
////        calculator.addLuggage(20.0); // Extra bag (Decision 6)
////        assertTrue(calculator.calculateTotalFee() > 0);
////
////        // Path to trigger isVip branch
////        calculator = new LuggageFareCalculator(TicketClass.BUSINESS, true);
////        calculator.addLuggage(30.0);
////        assertEquals(0.0, calculator.calculateTotalFee());
////    }
//
//    // Decision Coverage: Ensuring every branch (True/False) is taken
////    @Test
////    void testDecisionCoverage() {
////        // ticketClass == BUSINESS (True and False)
////        calculator = new LuggageFareCalculator(TicketClass.BUSINESS, false);
////        calculator.addLuggage(30.0);
////        assertEquals(0.0, calculator.calculateTotalFee());
////
////        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
////        calculator.addLuggage(20.0);
////        assertEquals(0.0, calculator.calculateTotalFee());
////
////        // isVip (True and False)
////        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
////        calculator.addLuggage(25.0);
////        assertEquals(0.0, calculator.calculateTotalFee());
////
////        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
////        calculator.addLuggage(25.0);
////        assertNotEquals(0.0, calculator.calculateTotalFee());
////    }
//
//    // Condition Coverage: Targeting compound conditions (&&)
//    @Test
//    void testConditionCoverage() {
//        // Condition: isExtraBag
//        // Case 1: isExtraBag=True (Economy, 2 bags) -> Fee added
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(10.0); // Bag 0 (Free)
//        calculator.addLuggage(10.0); // Bag 1 (Extra)
//        assertEquals(100.0, calculator.calculateTotalFee());
//
//        // Case 2: isExtraBag=True (Economy VIP, 3 bags) -> Fee added
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
//        calculator.addLuggage(10.0);
//        calculator.addLuggage(10.0);
//        calculator.addLuggage(10.0); // Bag 2 (Extra)
//        assertEquals(100.0, calculator.calculateTotalFee());
//
//        // Condition: weight > allowedWeight
//        // Case 3: weight > allowedWeight=True (Economy, 25kg > 23kg) -> Fee added
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(25.0);
//        assertEquals(100.0, calculator.calculateTotalFee()); // (25-23)*50 = 100.
//
//        // Case 4: weight > allowedWeight=True (VIP, 35kg - wait, max is 32)
//        // For VIP, allowedWeight is 32. To test weight > allowedWeight, we'd need > 32, but addLuggage throws.
//        // So for VIP, weight > allowedWeight is ALWAYS False because of addLuggage.
//        // This is a known limitation of the current limits.
//    }
//
//    // Independent Paths (McCabe / Basis Path Testing)
//    @Test
//    void testIndependentPaths() {
//        // Path 1: Empty luggage
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        assertEquals(0.0, calculator.calculateTotalFee());
//
//        // Path 2: Economy, non-VIP, 1 bag, not overweight
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(20.0);
//        assertEquals(0.0, calculator.calculateTotalFee());
//
//        // Path 3: Economy, non-VIP, 1 bag, overweight
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(24.0);
//        assertEquals(50.0, calculator.calculateTotalFee());
//
//        // Path 4: Economy, non-VIP, 2 bags, not overweight
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(20.0);
//        calculator.addLuggage(20.0);
//        assertEquals(100.0, calculator.calculateTotalFee());
//
//        // Path 5: Business, non-VIP, 1 bag, not overweight
//        calculator = new LuggageFareCalculator(TicketClass.BUSINESS, false);
//        calculator.addLuggage(30.0);
//        assertEquals(0.0, calculator.calculateTotalFee());
//
//        // Path 6: VIP, multiple bags, overweight
//        calculator = new LuggageFareCalculator(TicketClass.BUSINESS, true);
//        calculator.addLuggage(32.0); // Max allowed weight
//        calculator.addLuggage(32.0);
//        calculator.addLuggage(32.0);
//        assertEquals(0.0, calculator.calculateTotalFee());
//
//        // Path 7: Economy, VIP, multiple bags (to trigger extra bag logic within limits)
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
//        // Adăugăm 3 bagaje care respectă limita absolută de 32 kg ca să nu dea eroare addLuggage
//        calculator.addLuggage(30.0); // Bagaj 1: gratuit (limita este 32kg)
//        calculator.addLuggage(30.0); // Bagaj 2: gratuit (beneficiu VIP la Economy: 1 de bază + 1 bonus = 2 gratuite)
//        calculator.addLuggage(30.0); // Bagaj 3: Extra bag (limita de 2 bagaje a fost depășită)
//        // Fiindcă avem 1 extra bag (taxa 100.0) și 0 kg de supragreutate, totalul trebuie să fie exact 100.0
//        assertEquals(100.0, calculator.calculateTotalFee());
//
//        // Path 8: Economy, non-VIP, multiple bags, overweight (trigger both fees on the same bag)
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, false);
//        calculator.addLuggage(20.0); // Bagaj 1: gratuit
//        calculator.addLuggage(25.0); // Bagaj 2: Extra bag (100) + Overweight de 2kg (2*50=100)
//        assertEquals(200.0, calculator.calculateTotalFee());
//    }
//
//    // Utility Methods Coverage
//    @Test
//    void testUtilityMethods() {
//        calculator = new LuggageFareCalculator(TicketClass.ECONOMY, true);
//        assertEquals(TicketClass.ECONOMY, calculator.getTicketClass());
//        assertTrue(calculator.isVip());
//
//        calculator.addLuggage(10.0);
//        assertEquals(1, calculator.getLuggageCount());
//
//        calculator.resetLuggage();
//        assertEquals(0, calculator.getLuggageCount());
//    }
//}
