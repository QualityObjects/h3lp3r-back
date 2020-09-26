FROM maven:3-openjdk-11 as builder

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r application"

ENV WORKSPACE=/opt/workspace
ENV JARFILE=h3lp3r-back.jar
RUN mkdir -p $WORKSPACE/src
WORKDIR "$WORKSPACE"

ADD ./* $WORKSPACE/src/

RUN cd $WORKSPACE/src && ls -la && mvn clean package -B -DskipTests=true && mv target/*.jar $WORKSPACE/${JARFILE}
RUN ls -la .

FROM openjdk:11.0.8-jre-slim as executor

LABEL maintainer="tecnico@qualityobjects.com" \
      vendor="Quality Objects" \
      description="QO H3lp3r application"

ENV PORT=8080

ENV ES_URL=h3lp3r-es:9200
ENV SECRET=h3lp3r_super_secret
ENV JARFILE=h3lp3r-back.jar

RUN mkdir /opt/h3lp3r
WORKDIR /opt/h3lp3r
EXPOSE ${PORT}

COPY --from=builder /opt/workspace/${JARFILE} ${JARFILE}

RUN pwd && ls -lh

ENTRYPOINT ["bash", "-c" ]
CMD ["/usr/bin/java -jar ${JARFILE} --port=${PORT} --elasticsearch-host=${ES_URL} --secret=${SECRET}"]
