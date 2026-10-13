#!/bin/bash
# Stops TimeService gracefully (SIGTERM), then forcibly (SIGKILL) after STOP_TIMEOUT seconds (default 30).

APP_DIR=$(cd "$(dirname "$0")" && pwd) || exit 1
cd "$APP_DIR" || exit 1

PID_FILE='TimeService.pid'
STOP_TIMEOUT=${STOP_TIMEOUT:-30}

if [ -f "${PID_FILE}" ]; then
  PIDS=$(cat "${PID_FILE}")
else
  # Fallback for an instance started without the PID file
  PIDS=$(pgrep -f 'java .*-jar TimeService\.jar')
fi

ALIVE=()
for PID in ${PIDS}; do
  if [[ ${PID} =~ ^[0-9]+$ ]] && kill -0 "${PID}" 2>/dev/null; then
    ALIVE+=("${PID}")
  fi
done

if [ ${#ALIVE[@]} -eq 0 ]; then
  rm -f "${PID_FILE}"
  echo "Time Service is not running"
  exit 0
fi

for PID in "${ALIVE[@]}"; do
  kill -TERM "${PID}"
  for ((i = 0; i < STOP_TIMEOUT; i++)); do
    kill -0 "${PID}" 2>/dev/null || break
    sleep 1
  done
  if kill -0 "${PID}" 2>/dev/null; then
    echo "Time Service with PID=${PID} did not stop in ${STOP_TIMEOUT}s, killing it" >&2
    kill -KILL "${PID}"
  fi
  echo "Time Service with PID=${PID} has been stopped"
done

rm -f "${PID_FILE}"
exit 0
