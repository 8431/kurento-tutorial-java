#!/bin/bash
#
# Start Application Script
# Date: 2017/12/28

# Kurento 应用ID
export PROJECTID=kurento-one2one-call-recording

# 应用工作目录
export APP_PATH=$(dirname $(readlink -f $0))

# 应用进程 PID
export PIDFILE=/var/run/kurento/$PROJECTID.pid

if [ -e $PIDFILE ];then
		echo "Application is running"
        exit 1        
else
		cd $APP_PATH
		nohup mvn clean compile exec:java > $(date +%F).log 2>&1 &
		echo $! > $PIDFILE
        echo "Application is started OK."
fi
