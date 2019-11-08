FROM lost to the Docker
=======================

Meetup Command list
-------------------

    # List images
    docker images

    # Get image from repo
    docker pull mysql:5.7

    # Get image from repo
    docker rmi mysql:5.7

    #Hello word with contianers
    docker run -it --rm hairyhenderson/figlet "QOnecta !!"

    # Example with DataBase
    docker volume create mysql_project1_data_vol
    docker run --name project1-mysql -p 127.0.0.1:3306:3306 -v mysql_project1_data_vol:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=holahola -d mysql:8

    docker stop project1-mysql
    docker rm project1-mysql

    docker volume create pgdata1
    docker run --name qo-meetup-pg -v "pgdata1:/var/lib/postgresql/data" -p 5432:5432 -e POSTGRES_PASSWORD=holahola -d postgres:11 

    # Access to running container
    docker exec -it qo-meetup-pg bash

    # From psql command
    create database qo;
    create user qo with password 'qo';
    grant all on database qo to qo;

    docker volume create portainer_data
    docker run -d -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock --name portainer --restart always -v portainer_data:/data portainer/portainer
    #To access from browser: http://localhost:9000/

    docker run -it --rm --name=adminer -p 8081:8080 adminer

Build your own image

    docker build -t h3lp3r-web-tmp -t qualityobjects/h3lp3r-web-tmp --rm -f ./Dockerfile .
    #For testing
    docker run -it --rm -p 1080:80 h3lp3r-web-tmp

    docker push qualityobjects/h3lp3r-web-tmp:latest

    #From server
    docker run -it --name h3lper-web-tmp -p 1080:80 -d h3lp3r-web-tmp


docker-compose commands (from directory with docker-compose.yml file)

    docker-compose up -d # Create and run the containers
    docker-compose ps
    docker-compose stop h3lp3r-front
    docker-compose start h3lp3r-front
    docker-compose logs -f
    docker-compose logs -f h3lp3r-front
    docker-compose rm





