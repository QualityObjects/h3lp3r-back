#Change to directory with docker-compose.yml file

function redeploy {

    docker-compose pull $1

    docker-compose stop $1
    docker-compose rm -f $1
    docker-compose up -d $1
}

redeploy h3lp3r-front
redeploy h3lp3r-back