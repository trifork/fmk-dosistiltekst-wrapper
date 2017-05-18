fmk-dosistiltekst-wrapper
==============
fmk-dosistiltekst-wrapper udstiller et java API der indkapsler dosistiltekst javascript komponenten (se https://www.npmjs.com/package/fmk-dosis-til-tekst-ts ). P.t. understøttes Java 8.
JS komponenten selv kan hentes vha. "npm i fmk-dosis-til-tekst-ts". Javascriptfilen dosistiltekst.js findes hefter i node_modules/fmk-dosis-til-tekst-ts/target folderen.

Før brug skal DosisTilTekstWrapper klassen initialiseres med en java FileStream indeholdende dosistiltekst.js filen, eksempelvis:
```java
DosisTilTekstWrapper.initialize(new FileReader("node_modules/fmk-dosis-til-tekst-ts/target/dosistiltekst.js"));
```

Herefter kan de respektive hjælpe-metoder anvendes. De kaldes alle med en DosageWrapper instans som argument. DosageWrapper klasserne er identiske med de tidligere wrapper-klasser kendt fra den gamle dosis-til-tekst komponent, kun med ændret namespace. Det burde derfor være en forholdsvis enkel kodeopgave at skifte fra den gamle komponent til den nye, vha. dette projekt.

### Doseringsoversættelse

Eksempel på anvendelse, doseringsoversættelse:

```java
DosageWrapper dosage = DosageWrapper.makeDosage(
  StructuresWrapper.makeStructures(
    UnitOrUnitsWrapper.makeUnits("tablet", "tabletter"), 
      StructureWrapper.makeStructure(
        1, "ved måltid", 
        DateOrDateTimeWrapper.makeDate("2011-01-01"), DateOrDateTimeWrapper.makeDate("2011-01-30"), 
        DayWrapper.makeDay(1, 
          PlainDoseWrapper.makeDose(new BigDecimal(1)), 
          PlainDoseWrapper.makeDose(new BigDecimal(1)), 
          PlainDoseWrapper.makeDose(new BigDecimal(1), true)))));
  
String longText = DosisTilTekstWrapper.convertLongText(dosage);
String shortText = DosisTilTekstWrapper.convertShortText(dosage);
DailyDosis daily = DosisTilTekstWrapper.calculateDailyDosis(dosage);
DosageType dosageType = DosisTilTekstWrapper.getDosageType(dosage);
```
Desuden er der også mulighed for at hente kort og lang tekst + daglig dosis hhv. kombineret for fler-periode strukturerede doseringer, samt for hver enkelt periode:
```java
DosageTranslationCombined combined = DosisTilTekstWrapper.convertCombined(dosage);
```
### XML generering ud fra doseringsforslag

Eksempel på anvendelse:
```java
import dk.medicinkortet.fmkdosistiltekstwrapper.DosisTilTekstWrapper.FMKVersion;
...
SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

DosageProposalResult res = DosisTilTekstWrapper.getDosageProposalResult("PN", 1, "1", "tablet", "tabletter", ", tages med rigeligt vand", SIMPLE_DATE_FORMAT.parse("2017-05-17"), SIMPLE_DATE_FORMAT.parse("2017-06-01"), FMKVersion.FMK146, 1);

String xml = res.getXmlSnippet();
String longText = res.getLongText();
String shortText = res.getShortText();
```

## Kun til udviklere af selve fmk-dosistiltekst-wrapper komponenten:
Forudsætter fmk-dosis-til-tekst-ts er checket ud og bygget "parallelt" med dette projekt, således at ../fmk-dosis-til-tekst-ts/target/dosistiltekst.js er tilgængelig.