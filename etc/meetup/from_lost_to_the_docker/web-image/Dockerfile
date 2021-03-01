FROM nginx:latest

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r Site Maintenance"

COPY index.html /usr/share/nginx/html/index.html
RUN chmod 766 /usr/share/nginx/html/index.html
