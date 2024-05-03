#!/bin/bash

jira="LERN"
scope_regex="(deps|deps-dev|${jira}-[0-9]+)"
types="build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test"
message_regex="^(${types})\(${scope_regex}\): [a-zA-Z]"

if ! [[ "$1" =~ $message_regex ]]; then
  echo
  echo "Invalid commit message: $1"
  echo "Rules:"
  echo " ◆ Format: <type>(<scope>): <description>"
  echo " ◆ Types: ${types//|/, }"
  echo " ◆ Scope: ${jira} ticket number"
  echo
  exit 1
fi
