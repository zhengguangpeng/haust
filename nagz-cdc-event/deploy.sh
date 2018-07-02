#!/bin/sh 
#
# This shell script takes care of starting and stopping jar programs
#
# Version: v0.1.3
#
# Authors: [xuchang](mailto:xuc@yikeer.com)

#set -x 

PRO_NAME=nagz-cdc-event
MAIN_CLASS=com.hongtoo.nagz.cdc.event.CdcEventMain
#PRO_HOME=/home/gitlab-runner/${PRO_NAME}
#PRO_SOURCE=${CI_PROJECT_DIR}/${PRO_NAME}.jar

cd `dirname $0`

# export CLASSPATH
temp=.:$CLASSPATH
append(){
     temp=$temp":"$1
}
for i in lib/* ; do
  append $i
done
export CLASSPATH=$temp:./config

pro_pid() {
  echo `ps aux | grep $MAIN_CLASS | grep -v grep | awk '{print $2}'`
}

start() {
  pid=$(pro_pid)
  if [ -n "$pid" ];then
    echo "$PRO_NAME is already running (pid: $pid)"
  else
    echo "Starting $PRO_NAME"
    nohup java  $MAIN_CLASS &
  fi
  
  return 0
}

stop() {
  pid=$(pro_pid)
  if [ -n "$pid" ];then
    echo "Stoping $PRO_NAME"
	kill -9 $pid
  else
    echo "$PRO_NAME is not running"
  fi
  
  return 0
}

case $1 in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
    sleep 1
    start
    ;;
  status)
    pid=$(pro_pid)
    if [ -n "$pid" ];then
      echo "$PRO_NAME is runnint with pid: $pid"
      exit 0
    else
      echo "$PRO_NAME is not running"
      exit 3
    fi
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart|status}"
    exit 3
    ;;
esac

exit $?
