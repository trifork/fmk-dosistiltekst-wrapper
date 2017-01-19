# fmk-dosistiltekst-wrapper
fmk-dosistiltekst-wrapper udstiller et java API der indkapsler dosistiltekst javascript komponenten. P.t. understøttes Java 8.
JS komponenten selv kan hentes vha. "npm i fmk-dosis-til-tekst-ts". Javascriptfilen dosistiltekst.js findes hefter i node_modules/fmk-dosis-til-tekst-ts/target folderen.

Før brug skal DosisTilTekstWrapper klassen initialiseres med en java FileStream indeholdende dosistiltekst.js filen, eksempelvis:
```
DosisTilTekstWrapper.initialize(new FileReader("node_modules/fmk-dosis-til-tekst-ts/target/dosistiltekst.js"));
```

Herefter kan de respektive hjælpe-metoder anvendes. De kaldes alle med en DosageWrapper instans som argument. DosageWrapper klasserne er identiske med de tidligere wrapper-klasser kendt fra den gamle dosis-til-tekst komponent, kun med ændret namespace. Det burde derfor være en forholdsvis enkel kodeopgave at skifte fra den gamle komponent til den nye, vha. dette projekt.

Eksempel på anvendelse:

```
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

## Kun til udviklere af selve fmk-dosistiltekst-wrapper komponenten:
Forudsætter fmk-dosis-til-tekst-ts er checket ud og bygget "parallelt" med dette projekt, således at ../fmk-dosis-til-tekst-ts/target/dosistiltekst.js er tilgængelig.