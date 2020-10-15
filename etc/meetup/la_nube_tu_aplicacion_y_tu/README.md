La nube, tu aplicación y tú
=======================

Meetup Command list
-------------------

### Preparing the server

    # Login from your PC with any ssh client
    ssh ubuntu@xxx.xxx.xxx.xxx

**Basic security actions:**

From your PC create or locate an existing pair with private/public keys

    # From Windows Powershell or WSL2 console you can create it using:
    ssh-keygen -b 4096 -f myserver.key

From your new server within a ssh session:

    # Added public key to access without password
    # We copy-paste the  myserver.pub content in the file:
    vi ~/.ssh/authorized_keys 
    chmod 600 ~/.ssh/authorized_keys
    # Now (optionally) we can avoid the access using user/pass
    sudo vi /etc/ssh/sshd_config
    # We set to "no" the parameter
    PasswordAuthentication no

To apply changes
    sudo systemctl restart sshd

To verify all is working Ok, we should test the access with our new private key

    ssh -i ~/.ssh/myserver.key ubuntu@xxx.xxx.xxx.xxx

**Preparing the software requirements**

    sudo apt update && sudo apt upgrade
    sudo apt install nginx certbot python3-certbot-nginx docker-compose

If we want to run our app with ubuntu user we need to add it to the docker group    

    sudo usermod -G docker -a ubuntu
    #You need logout/login to apply the new group to user session

**Envirnment configuration**

Preparing docker-compose application, using the descriptor: [docker-compose.yml](https://gitlab.com/qo-oss/h3lp3r/h3lp3r-back/-/blob/master/etc/docker-compose/docker-compose.yml)

    mkdir ~/h3lp3r && cd ~/h3lp3r
    vi docker-compose.yml # Copy the content from GitLab
    sudo mkdir -p /opt/volumes/h3lp3r-es
    sudo chown -R ubuntu:docker /opt/volumes

Preparing nginx web server as reverse proxy with certificates for https access. We can use the config file from GitLab: [h3lp3r.conf](https://gitlab.com/qo-oss/h3lp3r/h3lp3r-back/-/blob/master/etc/nginx/h3lp3r.conf)
    
    sudo vi /etc/nginx/conf.d/h3lp3r.conf # We copy the content from GitLab file
    # We should use the propoer server_name parameter according to our DNS config
    sudo certbot --nginx # We should select the "force redirect" option in the wizard    

After we got the certificate we can test it accessing from a browser to: https://meetup2.qodev.es (choose the proper server name in your case). The certificate should be loaded by the browser but an error 504 (Bad Gateway) will appear until we start our app

We can enable the [HTTP/2](https://http2.github.io/) protocol which has some enhancements over the traditional HTTP1.1, editing the h3lp3r.conf file

    sudo vi /etc/nginx/conf.d/h3lp3r.conf

Modify the line:

    listen 443 ssl; # managed by Certbot

With:

    listen 443 ssl http2; # managed by Certbot

To apply changes:

    sudo systemctl restart nginx

We can check that the application resources are gzipped and served with the HTTP/2 protocol with Chrome DevTools Network console.

We should config an script to renew the SSL certificate automatically, we can use the crontab command:

    crontab -e

We add the line to try the update every day at 2:00 AM:

    0 2 * * *  /bin/certbot renew -n

**Application startup**

We've got everything ready to launch our application with docker-compose (with user `ubuntu`)

    cd ~/h3lp3r 
    docker-compose up -d
    docker-compose logs -f h3lp3r-back h3lp3r-front

If everything is OK our application should be live: https://meetup2.qodev.es

Our application access is protected with https.


**Docker-compose cheat sheet**

docker-compose commands (from directory with docker-compose.yml file)

    docker-compose up -d # Create and run the containers
    docker-compose ps
    docker-compose stop h3lp3r-front
    docker-compose start h3lp3r-front
    docker-compose logs -f
    docker-compose logs -f h3lp3r-front h3lp3r-server
    docker-compose rm





