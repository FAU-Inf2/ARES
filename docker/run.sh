#!/bin/bash

set -e
set -o pipefail

possible_arguments="test, executeExamples, readEvaluationResults, executeLaseEvaluation, executeJunitEvaluation"

if [ $# -lt 1 ] ; then
  echo "[!] you have to provide at least one argument: [$possible_arguments]"
  exit 1
fi

# check if the executing user is in the "docker" group
# if not, 'sudo' is required to run the docker container
if groups | grep -qE "\bdocker\b" ; then
  runcmd=
else
  runcmd="sudo"
fi

# run docker container
$runcmd docker run --rm --name "ARES" inf2/ares:1.0 $@
