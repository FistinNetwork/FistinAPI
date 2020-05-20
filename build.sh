#!/bin/bash
echo Building FistinAPI...
./gradlew build && ./gradlew shadowJar
echo Success !
