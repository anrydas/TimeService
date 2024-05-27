#!/bin/bash

PID=`ps ax | egrep 'java -jar.*TimeService' | grep -v grep | awk '{print $1}'`

re='^[0-9]+$'
if ! [[ $PID =~ $re ]] ; then
   echo "error: ${PID} is not a number" >&2; exit 1
fi

if [ -n "${PID}" ];then
    kill -9 ${PID}
    echo "Time Service with PID=${PID} have been stopped"
fi

exit 0
