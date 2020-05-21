#!/bin/bash
echo Building FistinAPI...
./gradlew build && ./gradlew shadowJar && ./gradlew sourcesJar
echo Success !
