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

    // calculeaza taxa suplimentara totala avand o structura optimizata pentru desenarea grafului de flux si calculul complexitatii
    public double calculateTotalFee() {
        if (luggageWeights.isEmpty()) {
            return 0.0; // iesire directa in cazul lipsei bagajelor
        }

        double totalFee = 0.0;

        // stabileste limita de greutate in functie de clasa
        double allowedWeight = (ticketClass == TicketClass.BUSINESS) ? MAX_WEIGHT_BUSINESS : MAX_WEIGHT_ECONOMY;

        // calculeaza numarul de bagaje gratuite
        int freeBagsAllowed = (ticketClass == TicketClass.BUSINESS) ? 2 : 1;
        if (isVip) {
            freeBagsAllowed += 1; // adaugare bagaj pentru statutul special unde se testeaza mutantii matematici
        }

        // bucla necesara pentru generarea ciclurilor in graf
        for (int i = 0; i < luggageWeights.size(); i++) {
            double weight = luggageWeights.get(i);
            boolean isExtraBag = i >= freeBagsAllowed;

            // taxa pentru depasirea numarului de bagaje
            if (isExtraBag && !isVip) {
                totalFee += EXTRA_BAG_FEE;
            }

            // taxa pentru depasirea greutatii permise
            if (weight > allowedWeight && !isVip) {
                totalFee += (weight - allowedWeight) * OVERWEIGHT_FEE_PER_KG;
            }
        }

        return totalFee;
    }

    public int getLuggageCount() {
        return luggageWeights.size();
    }

    public void resetLuggage() {
        luggageWeights.clear();
    }

    // metode de acces necesare pentru validarile din clasele de test
    public TicketClass getTicketClass() { return ticketClass; }
    public boolean isVip() { return isVip; }
}