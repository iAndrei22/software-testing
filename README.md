# Proiect Testarea Sistemelor Software - Luggage Fare Calculator

## Tema: Testare unitară în Java
## Membrii Echipei
- [Nume Membru 1]
- [Nume Membru 2]

## Descriere Proiect
Acest proiect implementează un sistem de calcul pentru taxele de bagaje ale unei companii aeriene. Logica de calcul ține cont de clasa biletului (Economy/Business), statutul de pasager VIP, greutatea fiecărui bagaj și numărul total de bagaje. Proiectul acoperă toate etapele de testare solicitate: funcțională (Black-Box), structurală (White-Box) și mutation testing.

## Configurație Sistem
- Sistem de Operare: Windows 10/11
- Limbaj de Programare: Java 21
- Build Tool: Maven 3.9.x
- Biblioteci și Tool-uri folosite:
    - JUnit 5.10.2 (Testing Framework)
    - PIT 1.15.8 (Mutation Testing)
    - JaCoCo (Code Coverage - via Maven)

Dependențele sunt gestionate prin `pom.xml`.

## Analiza Datelor de Intrare (Black-Box)

### Parametri de intrare (Inputs):
- TicketClass: Enum {ECONOMY, BUSINESS}
- isVip: Boolean
- weights: Listă de valori double (0.0 < weight <= 32.0)

### Clase de echivalență:
- **TicketClass**:
    - EC_1 = {ECONOMY} (1 bagaj gratuit, limită 23kg)
    - EC_2 = {BUSINESS} (2 bagaje gratuite, limită 32kg)
- **isVip**:
    - VIP_1 = {true} (0 taxe, +1 bagaj gratuit în calcul logic dar ignorat de taxe)
    - VIP_2 = {false} (se aplică taxe standard)
- **Weight**:
    - W_1 = (0, 23.0] (Valid Economy)
    - W_2 = (23.0, 32.0] (Overweight Economy, Valid Business)
    - W_3 = {w | w <= 0} (Invalid)
    - W_4 = {w | w > 32} (Invalid)
- **Luggage Count**:
    - C_1 = {0, 1} (Valid Economy free)
    - C_2 = {2} (Valid Business free, Extra Economy)
    - C_3 = {3, 4, 5} (Extra bags)
    - C_4 = {c | c > 5} (Invalid - Max limit)

### Analiza valorilor de frontieră (BVA):
- **Weight**: {0.0, 0.1, 22.9, 23.0, 23.1, 31.9, 32.0, 32.1}
- **Luggage Count**: {0, 1, 2, 5, 6}

## Testare Structurală (White-Box)

### Control Flow Graph (CFG)
Nodurile grafului corespund deciziilor din metoda `calculateTotalFee`.

[LOC PENTRU DIAGRAMA CFG - CFG/cfg_luggage.png]

Strategii implementate:
- **Statement Coverage**: Toate liniile de cod din `LuggageFareCalculator` sunt executate.
- **Decision Coverage**: Toate ramurile (True/False) pentru condițiile de clasă, VIP și greutate sunt acoperite.
- **Condition Coverage**: Testarea condițiilor compuse (ex: `isExtraBag && !isVip`).
- **Circuite Independente**: Testarea celor 8 căi de bază identificate prin complexitatea ciclamatică McCabe.

## Mutation Testing

### Tool și configurare
- **Tool**: `PIT` (Pitest) v1.15.8
- **Configurare**: Target classes: `LuggageFareCalculator`, Target tests: `functional.*`, `structural.*`, `mutation.*`.

### Procesul de mutation testing
Am utilizat PIT pentru a genera mutanți (schimbări de operatori, limite, valori de return). Testele din suita `mutation` au fost create special pentru a omorî mutanții care supraviețuiau suitei inițiale, inclusiv cei de pe logica VIP (linia 62 și 68) prin refactorizarea codului pentru a fi testabil.

**Rezultate PIT (Sumar):**
| Metrica | Valoare |
|---|---|
| Mutanți generați | ~30 |
| Mutanți omorâți | 100% |
| Mutation Score | 100% |

[LOC PENTRU CAPTURĂ ECRAN RAPORT PIT]

## Raport Utilizare AI
În cadrul proiectului am comparat testele scrise manual (`functional.FunctionalTests`, `structural.StructuralTests`) cu cele generate de AI (`ai.AIGeneratedTests`).

**Diferențe observate:**
- AI-ul a identificat rapid cazurile de `null` în constructor pe care testele manuale inițiale le omiseseră.
- Testele manuale sunt mai granulare în ceea ce privește uciderea mutanților specifici.
- AI-ul a folosit `DisplayName` și comentarii detaliate, îmbunătățind lizibilitatea raportului de testare.

[LOC PENTRU CAPTURI ECRAN RULARE COD AI]

## Rularea Testelor
```bash
mvn test                # Rulează toate testele
mvn pitest:mutationCoverage  # Rulează mutation testing și generează raport în target/pit-reports/
```

## Demo și Prezentare
- [Link Video Demo]
- [Link Prezentare PPT/PDF]
