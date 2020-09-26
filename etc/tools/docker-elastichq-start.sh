CONTAINER_NAME=elastichq


APP_CONTAINERS=$(docker ps -a | grep $CONTAINER_NAME | wc -l)
if [ $APP_CONTAINERS -gt 0 ]; then
    APP_CONTAINERS_RUNNING=$(docker ps | grep $CONTAINER_NAME | wc -l)
    if [ $APP_CONTAINERS_RUNNING -gt 0 ]; then
        echo "Container $CONTAINER_NAME is already running"
    else
        docker start $CONTAINER_NAME
    fi
else
    docker network create local 2> /dev/null
    docker run --name $CONTAINER_NAME --rm -d --network local -p 5000:5000 elastichq/elasticsearch-hq:latest
fi

docker ps | grep $CONTAINER_NAME