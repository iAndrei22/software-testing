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

Rularea raportului de acoperire a evidentiat o performanta ridicata in ambele suite, cu o atentie deosebita la detalii in suita AI:

- **Suita Proprie (si General):**  
  Conform raportului JaCoCo (`rezultateAI.jpeg`), am atins o acoperire a liniilor de cod de **98% (39 din 40 de linii)**.  
  Singura linie ramasa neacoperita se afla in constructor, la validarea clasei de zbor, unde un mutant a supravietuit prin modificarea conditiei de aruncare a exceptiei.

- **Suita AI:**  
  A contribuit decisiv la atingerea unei acoperiri de **97% pe instructiuni** si **92% pe ramuri (branches)**.  
  AI-ul a demonstrat o capacitate superioara de a genera teste pentru metodele utilitare (getteri, reset), asigurand ca aproape nicio linie de cod nu ramane netestata in fluxul principal.

---

## 3. Eficienta Mutation Testing (PIT Reports)

Rezultatele la nivel de mutanti ofera o perspectiva interesanta asupra profunzimii testelor:

- **Mutation Coverage:**  
  Conform raportului PIT (`pitTest.jpeg`), s-a obtinut un scor de **67%**, fiind omorati **20 din cei 30 de mutanti** generati.

- **Mutanti Supravietuitori:**  
  Cei 10 mutanti ramasi in viata (33%) sunt localizati in principal in zonele de logica conditionala complexa:
  - Conditiile de VIP (liniile 62-68), unde schimbarea operatorilor relationali sau a incrementarii nu a fost detectata de testele curente.
  - Boundary-ul pentru greutatea maxima in anumite configuratii de clasa.

- **Test Strength:**  
  Scorul de **83%** indica faptul ca testele existente sunt foarte eficiente pe codul pe care il acopera efectiv, dar exista inca "blind spots" in logica de business care permit supravietuirea unor mutatii subtile.

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
