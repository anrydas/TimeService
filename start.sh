#!/bin/sh

CONSOLE_LOG_FILE_NAME='logs/console.log'

if  [ -z ${CONSOLE_LOG_FILE_NAME} ]; then
  CONSOLE_LOG_FILE_NAME='/dev/null'
fi

nohup /home/das/jdk17/bin/java -jar TimeService.jar >> ${CONSOLE_LOG_FILE_NAME} &

exit 0
