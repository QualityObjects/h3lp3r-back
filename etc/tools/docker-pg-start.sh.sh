APPNAME=$1
VERSION=$2

if [ "$APPNAME" == "" ]; then
   echo "ERROR: Please set the app name: $0 [app_name] [postgres_version]?"
   echo "       For instance: $0 myapp 12"
   echo "       Previous command will launch a container from image 'postgres:12' and with name 'myapp-db'"
   echo "       Postgres version is optional by default is '11'"
   exit -1
fi
if [ "$VERSION" == "" ]; then
   export VERSION=11
fi

APP_CONTAINERS=$(docker ps -a | grep $APPNAME | wc -l)
if [ $APP_CONTAINERS -gt 0 ]; then
    APP_CONTAINERS_RUNNING=$(docker ps | grep $APPNAME | wc -l)
    if [ $APP_CONTAINERS_RUNNING -gt 0 ]; then
        echo "Container $APPNAME-db is already running"
    else
        docker start $APPNAME-db
    fi
else
    docker run --network=local --name $APPNAME-db -v /opt/volumes/$APPNAME-db:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_DB=$APPNAME -e POSTGRES_USER=$APPNAME -e POSTGRES_PASSWORD=$APPNAME -d postgres:$VERSION
fi

docker ps | grep $APPNAME