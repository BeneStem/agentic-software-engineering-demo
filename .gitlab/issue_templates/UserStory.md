/label ~"flow::backlog"~"estimate::needed"~"type::UserStory"

## Hintergrund / Ziel

Als (Nutzer) möchte ich (Funktion), damit/um/weil (Ziel)...

Anhand folgender KPIs kann der Erfolg der Features überprüft werden:

- KPI X → Zielwert Y

## Akzeptanz-Kriterien:

Auszufüllen:

- Akzeptanz-Kriterien
- Ableitungen aus der Bedrohungsanalyse
- Test(s) definiert und geschrieben
- Dokumentation liegt vor

### Tracking/DWH:

- Was soll getracked werden?
  - Anhand welcher KPIs kann der Erfolg der Features überprüft werden?
- Ist das DWH betroffen und ergeben sich Feldänderungen, die dort dokumentiert werden müssen?

### Monitoring:

- Was wird benötigt?
  1. SLIs: Performance, Verfügbarkeit, Alarme….

## Analyse

#### Ergebnis der Analyse:

- Sind alle externe Abhängigkeiten geklärt?
- Lösungsweg
- Komplexität in S-10/M-20/L-30 schätzen
- Integration alte vs. neuer Shop?
- Manuelles Testing notwendig?
- Datenschutz
- Dokumentationskonzept

#### Bedrohungsanalyse

- Evil User Stories → Als ein $AKTOR führe ich eine $AKTION durch um $ASSET zu schädigen/…
- Kritikalität
- Mitigation

## Definition of Ready:

- [ ] KPIs zur Erfolgskontrolle des Features wurden definiert
- [ ] Akzeptanzkriterien wurden festgelegt
- [ ] Analyse wurde durchgeführt
  - [ ] Bedrohungsanalyse wurde durchgeführt & Ergebnisse wurden abgeleitet
  - [ ] Datenschutz und Datensparsamkeit berücksichtigt
- [ ] Entwicklungsaufwand wurde geschätzt
- [ ] Alle externen Abhängigkeiten sind geklärt, keine Rückfragen mehr

## Definition of Done:

- [ ] Akzeptanzkriterien sind erfüllt
- [ ] Bedrohungsanalyse-Ergebnisse wurden umgesetzt
- [ ] Security Skills wurden berücksichtigt
- [ ] Tracking wurde implementiert
- [ ] Monitoring wurde aufgesetzt
- [ ] Test(s) geschrieben und erfolgreich bestanden
- [ ] Erfolgreich live deployt
- [ ] Dokumentation liegt vor
  - [ ] privacy port-Verfahren vollständig gepflegt
