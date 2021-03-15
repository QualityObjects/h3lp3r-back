FROM lost to the Docker
=======================

Meetup Command list
-------------------

Getting started 

    # List images
    docker images

    # Get image from remote repo
    docker pull mysql:5.7

    # Remove image from local repo
    docker rmi mysql:5.7

    #Hello word with containers
    docker run -it --rm hairyhenderson/figlet "Welcome to QOnecta !!"

    # Example with DataBase
    docker volume create mysql_project1_data_vol
    docker run --name project1-mysql --network local -p 127.0.0.1:3306:3306 -v mysql_project1_data_vol:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=holahola -d mysql:8

    docker stop project1-mysql
    docker rm project1-mysql

    docker run -it --name myweb -p 1080:80 nginx
    # Access with your browser to http://localhost:1080/

    docker exec -it myweb bash
    # cd /usr/share/nginx/html/
    # vi index.html # command not found
    # apt update && apt install -y vim 
    # vi index.html


Containers communication

    # local network to share docker LAN DNS/IP between containers
    docker network create local

    docker volume create pgdata1
    docker run --name qo-meetup-pg --network local -v "pgdata1:/var/lib/postgresql/data" -p 5432:5432 -e POSTGRES_PASSWORD=qo -e POSTGRES_DB=meetup -e POSTGRES_USER=qo -d postgres:11 

    #See logs
    docker logs -f qo-meetup-pg

    # Access to running container
    docker exec -it qo-meetup-pg psql -Uqo -W meetup

    #We can create a table for example:
    # create table hola (id int, name varchar);
    # insert into hola values (1, 'Meetup Docker'); insert into hola values (2, 'Meetup Docker 2020');

    docker volume create portainer_data
    docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock --name portainer --restart always -v portainer_data:/data portainer/portainer
    #To access from browser: http://localhost:9000/

    docker run -d --rm --network=local -e ADMINER_DESIGN='lucas-sandery' --name=adminer -p 8081:8080 adminer

Build your own image

    # Go to dir: etc/meetup/from_lost_to_the_docker/web-image
    docker build -t h3lp3r-web-tmp -t qualityobjects/h3lp3r-web-tmp --rm -f ./Dockerfile .
    #For testing
    docker run -it --rm -p 1080:80 h3lp3r-web-tmp

    #Publish to remote repo
    docker push qualityobjects/h3lp3r-web-tmp:latest

    #From server
    docker run --name h3lper-web-tmp -p 1080:80 -d qualityobjects/h3lp3r-web-tmp

    # Bulding h3lp3r-back
    docker build -t h3lp3r-back -t qualityobjects/h3lp3r-back --rm -f ./Dockerfile.complete .
    docker push qualityobjects/h3lp3r-back:latest

    # Bulding h3lp3r-front
    docker build -t h3lp3r-front -t qualityobjects/h3lp3r-front --rm -f ./Dockerfile.complete .
    docker push qualityobjects/h3lp3r-front:latest

docker-compose commands (from directory with docker-compose.yml file)

    docker-compose up -d # Create and run the containers
    docker-compose ps
    docker-compose stop h3lp3r-front
    docker-compose start h3lp3r-front
    docker-compose logs -f
    docker-compose logs -f h3lp3r-front
    docker-compose rm





