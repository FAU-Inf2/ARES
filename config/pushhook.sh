#!/bin/sh
./gradlew commitCheck test

RESULT=$?
if [ $RESULT -ne 0 ]; then echo "You have failed. Again."
fi
exit $RESULT
