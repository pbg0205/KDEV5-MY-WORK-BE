#!/bin/bash
JAR_PATH=/home/ec2-user/deploy/mywork-be-0.0.1-SNAPSHOT.jar
APP_NAME=mywork-be-0.0.1-SNAPSHOT

# 기존 프로세스 종료
PID=$(pgrep -f $APP_NAME)
if [ -n "$PID" ]; then
  kill -9 $PID
fi

# 백그라운드에서 실행
nohup java -jar $JAR_PATH --spring.config.additional-location=file:/home/ec2-user/config/application-prod.yml > /home/ec2-user/deploy/nohup.out 2>&1 &
