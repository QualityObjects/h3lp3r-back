APPNAME=h3lp3r
CONTAINER_NAME=$APPNAME-es


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
    docker run -d --rm --name $CONTAINER_NAME -v /opt/volumes/$CONTAINER_NAME:/usr/share/elasticsearch/data \
            --net local -p 9200:9200 -p 9300:9300 \
            -e "discovery.type=single-node" \
            -e "node.name=$CONTAINER_NAME" \
            -e "cluster.name=$APPNAME-cluster" \
            -e "ES_JAVA_OPTS=-Xms256m -Xmx512m" elasticsearch:7.9.1
fi

docker ps | grep $CONTAINER_NAME