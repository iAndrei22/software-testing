import java.util.ArrayList;
import java.util.List;

public class LuggageFareCalculator {

    // constante pentru analiza valorilor de frontiera si limite
    public static final double MAX_WEIGHT_ECONOMY = 23.0;
    public static final double MAX_WEIGHT_BUSINESS = 32.0;
    public static final double ABSOLUTE_MAX_WEIGHT = 32.0;

    public static final double OVERWEIGHT_FEE_PER_KG = 50.0;
    public static final double EXTRA_BAG_FEE = 100.0;
    public static final int MAX_BAGS_PER_PASSENGER = 5; // limita maxima de bagaje

    public enum TicketClass {
        ECONOMY, BUSINESS
    }

    // starea interna a obiectului
    private List<Double> luggageWeights;
    private TicketClass ticketClass;
    private boolean isVip;

    // constructor pentru initializarea datelor calculatorului
    public LuggageFareCalculator(TicketClass ticketClass, boolean isVip) {
        if (ticketClass == null) {
            throw new IllegalArgumentException("Clasa de zbor nu poate fi null");
        }
        this.ticketClass = ticketClass;
        this.isVip = isVip;
        this.luggageWeights = new ArrayList<>();
    }

    // adauga un bagaj nou in lista fiind util pentru testarea exceptiilor si analiza valorilor de frontiera
    public void addLuggage(double weight) {
        // verificare pentru numarul maxim de bagaje
        if (luggageWeights.size() >= MAX_BAGS_PER_PASSENGER) {
            throw new IllegalStateException("S-a atins numarul maxim de bagaje permise (" + MAX_BAGS_PER_PASSENGER + ").");
        }

        // conditie compusa pentru acoperirea conditiilor fiind utila pentru identificarea mutantilor pe limite
        if (weight <= 0.0 || weight > ABSOLUTE_MAX_WEIGHT) {
            throw new IllegalArgumentException("Greutate invalida. Trebuie sa fie in intervalul (0, 32] kg.");
        }
        luggageWeights.add(weight);
    }
}