#!/bin/bash

possible_arguments="test, executeExamples, readEvaluationResults, executeLaseEvaluation, executeJunitEvaluation"

if [ $# -lt 1 ] ; then
  echo "[!] you have to provide at least one argument: [$possible_arguments]"
  exit 1
fi

echo "--[ PULL FROM ARES REPOSITORY ]--"

cd repository/
git pull


echo
echo "--[ BUILD ARES ]--"

./gradlew build

echo
echo "--[ RUN ARES ]--"
./gradlew $@
