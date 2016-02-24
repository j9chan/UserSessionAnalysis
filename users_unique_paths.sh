#!/bin/bash

OPTS="-jar"

JAR="./target/UserSessionAnalysis-1.0-SNAPSHOT-jar-with-dependencies.jar"

java $OPTS $JAR $1 $2 $3
