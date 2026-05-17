<img width="801" height="447" alt="prompt" src="https://github.com/user-attachments/assets/b0dfa015-308b-4385-b252-50080532ab11" />
# Proiect Testarea Sistemelor Software - Luggage Fare Calculator

## Tema: Testare unitara in Java
## Membrii Echipei
- Bot George
- Ghenea Andrei
- Gheorghe Andrei
- Marian Rares
- Tudorache Andrei

## Descriere Proiect
Acest proiect implementeaza un sistem de calcul pentru taxele de bagaje ale unei companii aeriene. Logica de calcul tine cont de clasa biletului (Economy/Business), statutul de pasager VIP, greutatea fiecarui bagaj si numarul total de bagaje. Proiectul acopera toate cele 3 etape de implementare si testare: functionala, structurala si mutation testing.

## Configuratie Sistem
- Sistem de Operare: Windows 11
- Limbaj de Programare: Java 21
- Build Tool: Maven 3.9.x
- Biblioteci si Tool-uri folosite:
    - JUnit 5.10.2 (Testing Framework)
    - PIT 1.15.8 (Mutation Testing)
    - JaCoCo 0.8.12 (Code Coverage - via Maven)

Dependintele sunt gestionate prin `pom.xml`.

## Analiza Datelor de Intrare (Black-Box)

### Parametri de intrare (Inputs):
- TicketClass: Enum {ECONOMY, BUSINESS}
- isVip: Boolean
- weights: Lista de valori double (0.0 < weight <= 32.0)
- luggage count: intreg (0-5)

### Clase de echivalenta:
- **TicketClass**:
    - EC_1 = {ECONOMY} (1 bagaj gratuit, limita standard 23kg)
    - EC_2 = {BUSINESS} (2 bagaje gratuite, limita standard 32kg)
- **isVip**:
    - VIP_1 = {true} (Primeste limita de 32kg indiferent de clasa si +1 bagaj gratuit)
    - VIP_2 = {false} (Se aplica limitele standard de clasa)
- **Weight**:
    - W_1 = (0, 23.0] (Valid Economy)
    - W_2 = (23.0, 32.0] (Overweight Economy, Valid Business/VIP)
    - W_3 = {w | w <= 0} (Invalid - IllegalArgumentException)
    - W_4 = {w | w > 32} (Invalid - IllegalArgumentException)
- **Luggage Count**:
    - C_1 = {0, 1} (Valid Economy free)
    - C_2 = {2} (Valid Business/VIP free)
    - C_3 = {3} (Valid VIP free, Extra Business/Economy)
    - C_4 = {4, 5} (Extra bags)
    - C_5 = {c | c > 5} (Invalid - IllegalStateException)

### Analiza valorilor de frontiera (BVA):
- **Weight**: {0.0, 0.1, 22.9, 23.0, 23.1, 31.9, 32.0, 32.1}
- **Luggage Count**: {0, 1, 5, 6}

## Testare Structurala (White-Box)

### Control Flow Graph (CFG)
Nodurile grafului corespund deciziilor si instructiunilor din metoda `calculateTotalFee`.

<img width="1115" height="2560" alt="DiagramaCFG" src="https://github.com/user-attachments/assets/d6507b8c-5c2f-4e19-badb-c8e0750dc329" />


Strategii implementate:
- **Statement Coverage**: Toate instructiunile din `LuggageFareCalculator` sunt executate.
- **Decision Coverage**: Toate ramurile (True/False) pentru conditiile de clasa, VIP si greutate sunt acoperite.
- **Condition Coverage**: Testarea conditiilor compuse (ex: `ticketClass == TicketClass.BUSINESS || isVip`).
- **Circuite Independente**: Testarea cailor de baza conform complexitatii ciclamatice McCabe.

## Mutation Testing

### Tool si configurare
- **Tool**: `PIT` (Pitest) v1.15.8
- **Configurare**: Target class: `com.pm.LuggageFareCalculator`, Target tests: `mutation.MutationTests`.

### Procesul de mutation testing
Procesul a inclus generarea de mutanti si testarea lor cu suita dedicata. Conform rapoartelor, au fost generati 30 de mutanti, dintre care 20 au fost omorati, oferind o imagine clara asupra zonelor care necesita teste suplimentare (ex: conditii de limita nesecurizate).

**Rezultate PIT (Sumar):**
| Metrica | Valoare |
|---|---|
| Mutanti generati | 30 |
| Mutanti omorati | 20 |
| Mutation Score | 67% |
| Test Strength | 83% |

<img width="707" height="130" alt="Mutatii" src="https://github.com/user-attachments/assets/d2c7b861-fb98-4efa-91d7-fa053259c53f" />
<img width="822" height="302" alt="pitTest" src="https://github.com/user-attachments/assets/53759c05-17ef-4dda-833f-eed788efdd55" />


## Raport Utilizare AI
In cadrul proiectului am utilizat AI-ul pentru a genera o suita de teste (`ai.LuggageFareCalculatorAITest`) si pentru a analiza codul sursa (`Documentatie/RaportAnalizaAI.md`).

Promptul catre Gemini este: 

<img width="801" height="447" alt="prompt" src="https://github.com/user-attachments/assets/f3262ab1-b9b1-44bd-bac6-d4f2b30a0783" />


**Rezultate Coverage (JaCoCo):**
- **Instruction Coverage**: 97%
- **Branch Coverage**: 92%
- **Lines Coverage**: 98% (39/40)

**Diferente observate:**
- AI-ul a identificat rapid cazurile limita (null in constructor, greutati negative), contribuind la scorul ridicat de coverage.
- Testele manuale sunt esentiale pentru cresterea scorului de mutatie (67%), deoarece AI-ul tinde sa ignore anumite mutatii subtile de operatori.
- Documentatia generata de AI ofera o perspectiva clara asupra structurii proiectului.

<img width="1142" height="97" alt="rezultateAI" src="https://github.com/user-attachments/assets/c8929371-742d-4acb-9b47-51210559954e" />


## Rularea Testelor
```bash
mvn test                     # Ruleaza toate testele (Functional, Structural, Mutation, AI)
mvn jacoco:report            # Genereaza raportul de coverage in target/site/jacoco/
mvn pitest:mutationCoverage  # Ruleaza mutation testing si genereaza raport in target/pit-reports/
```

### Capturi de ecran rezultate:

**Rulare Teste (Coverage detaliat pe metode):**
<img width="1226" height="232" alt="rezultateTeste" src="https://github.com/user-attachments/assets/43eddc8e-5f54-4f14-9061-5af82d2f3e83" />


## Demo si Prezentare
- [Link Video Demo] - `Demo/Demo.mkv`
- [Link Prezentare PPT/PDF] - `Documentatie/Documentatie.pdf`
