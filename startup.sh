#!/bin/bash
echo "Starting Azure App Service..."
echo "Java Version:"
java -version
echo "Environment Variables:"
printenv | grep -E "(SPRING|DATABASE|PORT|AZURE)" | sort
echo "Starting application..."
exec java -Xmx512m -Xms256m -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-minimal} -jar /home/site/wwwroot/app.jar 