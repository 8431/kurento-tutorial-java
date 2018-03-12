#!/bin/bash
#
# Stop Application Script
# Date: 2017/12/28

# Kurento 应用ID
export PROJECTID=kurento-Monitor

# 应用进程 PID
export PIDFILE=/var/run/kurento/$PROJECTID.pid

# 停止进程
if [ -e $PIDFILE ];then
        # kill 进程
        kill $(cat $PIDFILE)
        rm -f $PIDFILE
        echo "Application is stopped OK."
else
        echo "Application is not running"
        exit 1
fi
