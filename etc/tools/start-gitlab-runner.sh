docker run -d --name gitlab-runner --restart unless-stopped \
     -v /opt/volumes/gitlab-runner/config:/etc/gitlab-runner:Z \
     -v /var/run/docker.sock:/var/run/docker.sock \
     gitlab/gitlab-runner:alpine
