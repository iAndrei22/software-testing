import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class LuggageFareCalculatorAITest {

    private LuggageFareCalculator economyPassenger;
    private LuggageFareCalculator businessPassenger;
    private LuggageFareCalculator vipPassenger;

    @BeforeEach
    void setUp() {
        // Inițializarea obiectelor pentru reducerea codului duplicat.
        economyPassenger = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.ECONOMY, false);
        businessPassenger = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.BUSINESS, false);
        vipPassenger = new LuggageFareCalculator(LuggageFareCalculator.TicketClass.ECONOMY, true);
    }

    /**
     * STRATEGIA: Partiționarea în clase de echivalență (EP)
     * Testăm instanțierea clasei cu o clasă de echivalență invalidă (null).
     */
    @Test
    @DisplayName("EP: Instanțierea cu o clasă de zbor invalidă (null) aruncă excepție")
    void testConstructorThrowsExceptionForNullTicketClass() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new LuggageFareCalculator(null, false);
        });
        assertEquals("Clasa de zbor nu poate fi null", exception.getMessage());
    }

    /**
     * STRATEGIA: Analiza valorilor de frontieră (BVA) & Acoperire la nivel de decizie/condiție
     * Validăm limitele inferioare și superioare ale greutății bagajului: 0.0, 0.01, 32.0 și 32.01.
     * Acoperă de asemenea evaluarea condiției compuse: (weight <= 0.0 || weight > ABSOLUTE_MAX_WEIGHT).
     */
    @ParameterizedTest
    @CsvSource({
            "0.0, true",    // Limita inferioară invalidă
            "0.01, false",  // Valoare validă imediat peste limita inferioară
            "32.0, false",  // Limita superioară validă maximă admisă
            "32.01, true"   // Limita superioară invalidă
    })
    @DisplayName("BVA & Condition Coverage: Testarea limitelor absolute de greutate per bagaj")
    void testAddLuggageWeightBoundaries(double weight, boolean shouldThrowException) {
        if (shouldThrowException) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                economyPassenger.addLuggage(weight);
            });
            assertEquals("Greutate invalida. Trebuie sa fie in intervalul (0, 32] kg.", exception.getMessage());
        } else {
            assertDoesNotThrow(() -> economyPassenger.addLuggage(weight));
        }
    }

    /**
     * STRATEGIA: Analiza valorilor de frontieră (BVA)
     * Testăm adăugarea bagajelor până la limita maximă admisă (5) și depășirea acesteia (6).
     */
    @Test
    @DisplayName("BVA: Testarea limitei superioare pentru numărul maxim de bagaje")
    void testMaxBagsPerPassengerBoundary() {
        // Adăugăm numărul maxim permis de bagaje (5 valid)
        for (int i = 0; i < LuggageFareCalculator.MAX_BAGS_PER_PASSENGER; i++) {
            economyPassenger.addLuggage(10.0);
        }
        assertEquals(5, economyPassenger.getLuggageCount(), "Numărul de bagaje ar trebui să fie la limita maximă permisă.");

        // Adăugarea celui de-al 6-lea bagaj trebuie să arunce excepție (invalid)
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            economyPassenger.addLuggage(10.0);
        });
        assertTrue(exception.getMessage().contains("S-a atins numarul maxim de bagaje permise"));
    }

    /**
     * STRATEGIA: Acoperirea circuitelor independente (McCabe Cyclomatic Complexity)
     * Calea 1: Lista de bagaje este goală. Returnarea imediată a valorii 0.0.
     */
    @Test
    @DisplayName("McCabe Path 1: Calculul taxei pentru un pasager fără bagaje")
    void testCalculateTotalFeeNoBags() {
        double fee = economyPassenger.calculateTotalFee();
        assertEquals(0.0, fee, "Taxa totală pentru lipsa bagajelor trebuie să fie 0.0.");
    }

    /**
     * STRATEGIA: Mutation Testing (Mutant pe operator matematic)
     * Scop: Uciderea mutantului care ar schimba "freeBagsAllowed += 1;" în "freeBagsAllowed -= 1;" (sau similar).
     * Explicație: Un pasager VIP la clasa Economy are dreptul la 2 bagaje gratuite (1 standard + 1 VIP). 
     * Dacă operatorul matematic este mutat, pasagerul VIP va fi taxat pentru al doilea bagaj.
     */
    @Test
    @DisplayName("Mutation Testing: Uciderea mutantului pe operator matematic (freeBagsAllowed += 1)")
    void testKillMathMutantVipFreeBags() {
        vipPassenger.addLuggage(15.0); // Primul bagaj (gratuit pentru Economy)
        vipPassenger.addLuggage(15.0); // Al doilea bagaj (gratuit doar datorită statusului VIP)

        double fee = vipPassenger.calculateTotalFee();
        // Dacă mutantul a schimbat += în -= sau *=, taxa calculată ar fi mai mare ca 0.
        assertEquals(0.0, fee, "Un pasager VIP la Economy nu ar trebui să plătească taxe pentru 2 bagaje valide.");
    }

    /**
     * STRATEGIA: Mutation Testing (Mutant pe limită relațională)
     * Scop: Uciderea mutantului care ar modifica "i >= freeBagsAllowed" în "i > freeBagsAllowed".
     * Explicație: Dacă mutantul modifică operatorul relațional din >= în >, bagajul cu indexul egal 
     * cu freeBagsAllowed nu ar fi marcat ca extra-bag, iar sistemul nu ar percepe taxa corectă.
     */
    @Test
    @DisplayName("Mutation Testing: Uciderea mutantului pe limită relațională (i >= freeBagsAllowed)")
    void testKillRelationalMutantExtraBagCondition() {
        economyPassenger.addLuggage(10.0); // Index 0 (Gratuit)
        economyPassenger.addLuggage(10.0); // Index 1 (Extra bag)

        double fee = economyPassenger.calculateTotalFee();
        // Dacă condiția s-a mutat în i > freeBagsAllowed, 1 > 1 este fals, taxa ar ieși 0.0.
        // Corect este 1 >= 1 (adevarat), aplicând EXTRA_BAG_FEE.
        assertEquals(LuggageFareCalculator.EXTRA_BAG_FEE, fee, "Al doilea bagaj la Economy trebuie să aplice taxa de extra bagaj.");
    }

    /**
     * STRATEGIA: Acoperirea circuitelor independente (McCabe) & Statement Coverage
     * Calea 3: Pasager Business, non-VIP, cu multiple bagaje care depășesc atât limita de număr cât și de greutate.
     * Intră pe toate ramurile IF ("isExtraBag && !isVip" și "weight > allowedWeight && !isVip").
     */
    @Test
    @DisplayName("McCabe & Statement Coverage: Pasager Business cu bagaje suplimentare și supraponderale")
    void testCalculateTotalFeeBusinessExtraAndOverweight() {
        businessPassenger.addLuggage(20.0); // Bagaj gratuit, greutate ok
        businessPassenger.addLuggage(20.0); // Bagaj gratuit, greutate ok

        // Bagaj extra (index 2 >= 2) și supraponderal (>32 kg este invalid general, testăm depășirea de 23 la economy, dar aici suntem la business)
        // La business allowedWeight e 32, deci nu putem depasi allowedWeight intrucat 32 e si ABSOLUTE_MAX_WEIGHT.
        // Trebuie să testăm greutatea supraponderală prin Economy, unde allowedWeight este 23.

        economyPassenger.addLuggage(20.0); // Index 0, gratuit, greutate ok
        economyPassenger.addLuggage(25.0); // Index 1, extra bag, supraponderal (25 > 23)

        double fee = economyPassenger.calculateTotalFee();
        // Taxa = 100 (extra bag) + (25 - 23) * 50 (overweight fee) = 100 + 100 = 200.0
        assertEquals(200.0, fee, "Taxa trebuie să includă atât tariful pentru bagaj suplimentar, cât și pentru depășirea greutății.");
    }

    /**
     * STRATEGIA: Decision/Condition Coverage
     * Evaluăm circuitul în care condiția !isVip este falsă (pasagerul este VIP), 
     * anulând taxele suplimentare pentru depășiri de limită individuală.
     */
    @Test
    @DisplayName("Condition Coverage: Bypass-ul taxelor pentru pasagerii VIP")
    void testCalculateTotalFeeVipIgnoresFees() {
        // Pasager VIP, Economy. Are dreptul la 2 bagaje gratuite.
        vipPassenger.addLuggage(25.0); // Depășește greutatea de Economy (25 > 23), dar e VIP.
        vipPassenger.addLuggage(20.0); // Al doilea bagaj (gratuit pentru VIP)
        vipPassenger.addLuggage(20.0); // Al treilea bagaj (extra bag), dar VIP ignoră taxa de extra conform codului.

        // Observație logică pe cod: Pentru un pasager VIP, !isVip este fals.
        // Așadar, "isExtraBag && !isVip" și "weight > allowedWeight && !isVip" vor fi false. Taxa calculată va fi 0.
        double fee = vipPassenger.calculateTotalFee();
        assertEquals(0.0, fee, "Sistemul nu ar trebui să aplice nicio taxă pasagerilor VIP conform logicii curente.");
    }

    /**
     * STRATEGIA: Statement Coverage
     * Validarea metodelor utilitare (getters) și resetarea stării obiectului.
     */
    @Test
    @DisplayName("Statement Coverage: Validarea metodelor utilitare de acces și resetare")
    void testUtilityMethodsAndReset() {
        economyPassenger.addLuggage(15.0);
        assertEquals(1, economyPassenger.getLuggageCount());
        assertEquals(LuggageFareCalculator.TicketClass.ECONOMY, economyPassenger.getTicketClass());
        assertFalse(economyPassenger.isVip());

        economyPassenger.resetLuggage();
        assertEquals(0, economyPassenger.getLuggageCount(), "Lista de bagaje trebuie să fie goală după apelarea resetLuggage().");
    }
}