# Analiză Comparativă Teste Unitare AI vs. Manual

## 1. Strategii de Testare și Structură

Ambele suite de teste au utilizat strategii fundamentale de testare "black-box", însă cu abordări ușor diferite în implementare:

- **Analiza Valorilor de Frontieră (BVA):**  
  Ambele suite folosesc teste parametrizate (`@ParameterizedTest`) pentru a valida limitele de greutate ale bagajelor (0.0, 0.01, 32.0 și 32.01 kg).  
  Această abordare este corectă în ambele cazuri, asigurând că sistemul reacționează conform specificațiilor la limitele intervalului permis (0, 32].

- **Partiționarea în Clase de Echivalență (EP):**  
  Ambele suite includ teste pentru validarea constructorului cu parametri invalizi (`null`), aruncând `IllegalArgumentException`.

- **Complexitatea Ciclomatică (McCabe):**  
  Atât suita noastră, cât și cea AI, au identificat corect căile independente prin metoda `calculateTotalFee`, inclusiv cazul în care lista de bagaje este goală.  
  Suita AI este însă mai explicită în comentarii, marcând clar fiecare cale analizată conform strategiei McCabe.

- **Testarea Mutațiilor (Mutation Testing):**  
  Ambele fișiere conțin teste concepute specific pentru a "ucide" mutanții generați de instrumentul PIT.  
  În suita proprie, am definit teste precum `testKillWeightZeroMutant` pentru a verifica limitele relaționale.  
  Suita AI a inclus teste similare, dar s-a concentrat mai mult pe mutanții de tipul modificării operatorilor matematici în calculul bagajelor gratuite pentru pasagerii VIP.

---

## 2. Analiza Acoperirii Codului (Code Coverage)

Rularea raportului de acoperire a evidențiat o diferență cantitativă între cele două suite:

- **Suita Proprie:**  
  A atins o acoperire a liniilor de cod de **94% (31 din 33 de linii)**.  
  Analiza detaliată a raportului PIT arată că au rămas neacoperite metodele utilitare de acces (`getters`) `getTicketClass` și `isVip`.

- **Suita AI:**  
  A obținut o acoperire de **100% (33 din 33 de linii)**.  
  Această performanță superioară se datorează includerii metodei `testUtilityMethodsAndReset`, care verifică explicit toți getterii și resetarea stării obiectului, asigurându-se că nicio linie de cod nu rămâne netestată.

---

## 3. Eficiența Mutation Testing (PIT Reports)

Deși acoperirea liniilor diferă, rezultatele la nivel de mutanți sunt surprinzător de similare:

- **Mutation Coverage:**  
  Ambele suite au obținut un scor de **84%**, reușind să elimine **26 din cei 31 de mutanți posibili**.

- **Mutanți Supraviețuitori:**  
  În ambele cazuri, au supraviețuit mutanți la:
  - linia 62 (modificarea incrementării `freeBagsAllowed += 1`)
  - linia 68 (negarea condiției de extra bagaj în anumite contexte)

  Acest lucru sugerează că, deși AI-ul este mai riguros în a atinge 100% line coverage, acesta întâmpină aceleași dificultăți logice ca și testarea manuală în a ucide mutanții din condiții logice compuse.

- **Test Strength:**  
  - Suita proprie: **93%**
  - Suita AI: **84%**

  Aceasta indică faptul că testele scrise manual au fost mai eficiente în a analiza codul pe care l-au acoperit efectiv, în timp ce suita AI a inclus mai multe teste simple (precum cele pentru getteri) care cresc acoperirea liniilor fără a adăuga complexitate logică mare.

---

## 4. Concluzii Qualitative

Din analiza noastră asupra codului generat de AI față de cel scris manual, putem concluziona:

- **Documentare:**  
  Suita AI este mai bine documentată, folosind adnotări `@DisplayName` sugestive și Javadoc pentru a explica strategia din spatele fiecărui test.

- **Completitudine:**  
  AI-ul a demonstrat o capacitate superioară de a identifica metodele "uitate" (getter-ii), ceea ce este esențial pentru atingerea metricilor de acoperire totală.

- **Calitatea Testelor:**  
  Testarea manuală a rămas la fel de solidă în detectarea erorilor logice critice, dovadă fiind Mutation Coverage-ul identic.

- **Utilitate:**  
  Instrumentul AI s-a dovedit a fi un asistent extrem de eficient în generarea rapidă a codului de tip "boilerplate" și în asigurarea unei acoperiri exhaustive a liniilor de cod, însă necesită în continuare supraveghere umană pentru a aborda mutanții logici cei mai complecși.
