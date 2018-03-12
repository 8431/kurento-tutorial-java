#!/bin/bash
# 
# Automated deployed application scripts
# Date: 2017/12/18
# Update Date: 2017/12/28 修改了应用工作目录及备份目录，添加了停止应用后的返回提示信息。

# Kurento 应用ID
export PROJECTID=kurento-one2one-call-recording

# 应用工作目录
export APP_PATH=$(cd $(dirname $0); pwd)

# 应用备份目录
export APP_BACKUP_PATH=$(dirname $APP_PATH)/backup/$(date +%F)

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
fi

# 如果备份目录不存在，创建之
[ -d "$APP_BACKUP_PATH" ] || mkdir -pv $APP_BACKUP_PATH

# 备份
if [ $? -eq 0 ];then
	tar -zcf $APP_BACKUP_PATH/$PROJECTID-$(date +%H%M%S).tar.gz $APP_PATH
else 
	echo "Backup failure"
	exit 1
fi

# 同步代码
if [ $? -eq 0 ];then
	cd $APP_PATH && svn up
	echo "SVN Update finish."
else
	echo "SVN Update failure."
	exit 2
fi

# 代码同步成功，启动应用
nohup mvn clean compile exec:java > $(date +%F).log 2>&1 &
echo $! > $PIDFILE

# 睡眠 20 秒，等待应用启动完成
sleep 20

# 截取启动日志输出给 Jenkins 控制台查看
tail -n 50 $(date +%F).log

# 返回状态值
export APP_PID=$(netstat -tunl | grep "\<9999\>" &> /dev/null; echo $?)

if [ ${APP_PID} -eq 0 ]; then
    echo "Deploy Success!"
else
    echo "Deploy Failed!"
	exit 1
fi