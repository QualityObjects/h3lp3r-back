FROM openjdk:11.0.8-jre-slim

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r application"

ENV PORT=8080
ENV ES_URL=h3lp3r-es:9200
ENV SECRET=h3lp3r_super_secret

ARG JAR_FILE=h3lp3r-back.jar
ENV APP_JARFILE=${JAR_FILE}

RUN mkdir /opt/h3lp3r
WORKDIR /opt/h3lp3r
EXPOSE ${PORT}

COPY ./${APP_JARFILE} .

RUN pwd && ls -lh

ENTRYPOINT ["bash", "-c" ]
CMD ["java -jar ${APP_JARFILE} --port=${PORT} --elasticsearch-host=${ES_URL} --secret=${SECRET}"]
