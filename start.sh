#!/bin/sh
# Starts TimeService in the background from the directory this script is in.
# Optional env: JAVA_HOME, JAVA_OPTS, CONSOLE_LOG_FILE_NAME (default logs/console.log, empty = discard output).

APP_DIR=$(cd "$(dirname "$0")" && pwd) || exit 1
cd "$APP_DIR" || exit 1

JAR_NAME='TimeService.jar'
PID_FILE='TimeService.pid'
CONSOLE_LOG_FILE_NAME=${CONSOLE_LOG_FILE_NAME-logs/console.log}

if [ -n "${JAVA_HOME}" ]; then
  JAVA="${JAVA_HOME}/bin/java"
else
  JAVA=$(command -v java)
fi
if [ -z "${JAVA}" ] || [ ! -x "${JAVA}" ]; then
  echo "error: java not found; set JAVA_HOME or add java to PATH" >&2
  exit 1
fi

if [ ! -f "${JAR_NAME}" ]; then
  echo "error: ${APP_DIR}/${JAR_NAME} not found" >&2
  exit 1
fi

if [ -f "${PID_FILE}" ]; then
  OLD_PID=$(cat "${PID_FILE}")
  if [ -n "${OLD_PID}" ] && kill -0 "${OLD_PID}" 2>/dev/null; then
    echo "Time Service is already running with PID=${OLD_PID}"
    exit 0
  fi
  rm -f "${PID_FILE}"
fi

if [ -z "${CONSOLE_LOG_FILE_NAME}" ]; then
  CONSOLE_LOG_FILE_NAME='/dev/null'
else
  mkdir -p "$(dirname "${CONSOLE_LOG_FILE_NAME}")" || exit 1
fi

# JAVA_OPTS is intentionally unquoted to allow several options
nohup "${JAVA}" ${JAVA_OPTS} -jar "${JAR_NAME}" >> "${CONSOLE_LOG_FILE_NAME}" 2>&1 &
PID=$!
echo "${PID}" > "${PID_FILE}"

sleep 2
if ! kill -0 "${PID}" 2>/dev/null; then
  rm -f "${PID_FILE}"
  echo "error: Time Service exited right after start, see ${CONSOLE_LOG_FILE_NAME}" >&2
  exit 1
fi

echo "Time Service has been started with PID=${PID}"
exit 0
