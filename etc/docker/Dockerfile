FROM ubuntu:18.04 as builder

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r application"

#Instalamos el repo de node 10.x
RUN apt update -y && apt install -y curl

RUN apt install -y openjdk-11-jre-headless maven && apt clean

ENV WORKSPACE=/opt/workspace
ARG back_project_sources=h3lp3r-back-sources.tgz
ENV JARFILE=h3lp3r-back.jar
RUN mkdir $WORKSPACE
WORKDIR "$WORKSPACE"

ADD $back_project_sources $WORKSPACE

RUN cd $WORKSPACE; ls -l

RUN cd h3lp3r-back && mvn package -B -DskipTests=true && mv target/*.jar ../${JARFILE}
RUN ls -la /opt/

FROM ubuntu:18.04 as executor

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r application"

RUN apt update -y && apt install -y curl openjdk-11-jre-headless && apt clean

ENV PORT=8080

ENV DB_HOST=localhost
ENV DB_NAME=h3lp3r
ENV DB_USER=h3lp3r
ENV DB_PASSWORD=h3lp3r
ENV SECRET=h3lp3r_super_secret
ENV JARFILE=h3lp3r-back.jar

RUN mkdir /opt/h3lp3r
WORKDIR /opt/h3lp3r
EXPOSE ${PORT}

COPY --from=builder /opt/workspace/${JARFILE} ${JARFILE}

RUN pwd && ls -lh

ENTRYPOINT ["bash", "-c" ]
CMD ["/usr/bin/java -jar ${JARFILE} --port=${PORT} --db-host=${DB_HOST} --db-name=${DB_NAME} --db-user=${DB_USER} --db-password=${DB_PASSWORD} --secret=${SECRET}"]
