#!/bin/bash
cd /home/kavia/workspace/code-generation/taskflow-107818-070430cd/taskflow
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

