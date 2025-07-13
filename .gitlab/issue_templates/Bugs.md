/label ~"flow::backlog"~"estimate::needed"~"type::Bug"

## Fehlerbeschreibung / Reproduktion

...

## Monitoring:

- Falls noch nicht gemonitored, wie kann der Bug zukünftig einen Alert auslösen?

## Kritikalität

- Wie groß ist der Business-Impact?
    - Wie hoch ist der Anteil der User, die betroffen sind / nicht mehr bestellen können?
    - Wie schwer blockiert der Bug die Conversion?
    - Wie hoch ist die potenzielle Umsatzeinbuße?
    - Werden Gesetze verletzt? Stellt es ein Sicherheitsrisiko dar?
- Wird sich der Effekt verstärken, wenn wir noch länger warten?
- Wie hoch ist die Auswirkung auf den Kundeservice? (Call-Center = Kosten)
- Ist das Entwicklungsteam / die Produktion / der Kundenservice wesentlich in seiner Arbeit behindert?

## Analyse

### Ergebnis der Analyse:

- Müssen Tests angepasst werden?
- Manuelles Testing notwendig?
- Post Mortem notwendig? Falls ja, [liegt hier das Template](https://dokumentation.ecom.blume2000.de/uebergreifend/postmortem/Template.html).
- Lösungsweg
- Komplexität in S-10/M-20/L-30 schätzen

## Definition of Ready:

- [ ] Kritikalität geprüft
- [ ] Analyse wurde durchgeführt
- [ ] Entwicklungsaufwand wurde geschätzt

## Definition of Done:

- [ ] Security Skills wurden berücksichtigt
- [ ] Monitoring wurde ggf. aufgesetzt
- [ ] Test(s) ggf. angepasst und erfolgreich bestanden
- [ ] Erfolgreich live deployt und gefixt
- [ ] Post Mortem angelegt
