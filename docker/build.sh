#!/bin/bash

set -e
set -o pipefail

# check if the executing user is in the "docker" group
# if not, 'sudo' is required to build the docker container
if groups | grep -qE "\bdocker\b" ; then
  runcmd=
else
  runcmd="sudo"
fi

# create docker container
$runcmd docker build -t inf2/ares:1.0 .
