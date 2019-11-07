FROM nginx:latest

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r Site Maintenance"

RUN rm /etc/nginx/conf.d/default.conf
COPY default.conf /etc/nginx/conf.d/default.conf
RUN chmod 644 /etc/nginx/conf.d/*

COPY index.html /usr/share/nginx/html/index.html
RUN chmod 766 /usr/share/nginx/html/index.html
