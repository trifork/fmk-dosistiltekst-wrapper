fmk-dosistiltekst-wrapper
==============
fmk-dosistiltekst-wrapper udstiller et java API der indkapsler dosistiltekst javascript komponenten (se https://www.npmjs.com/package/fmk-dosis-til-tekst-ts ). Dette projekt indeholder i sig selv kun en række fælles API klasser. Afhængig af, hvilken java version der anvendes, skal man desuden hente enten fmk-dosis-tiltekst-wrapper-nashorn (Java 11 og højere) eller fmk-dosistiltekst-rhino (Java 6 eller 7).
Se også de respektive projekter for kodeeksempler på anvendelsen.

## Kun til udviklere af selve fmk-dosistiltekst-wrapper komponenten:
Forudsætter fmk-dosis-til-tekst-ts er checket ud og bygget "parallelt" med dette projekt, således at ../fmk-dosis-til-tekst-ts/target/dosistiltekst.js er tilgængelig.