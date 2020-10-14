FROM openjdk:11.0.8-jre-slim as executor

ADD target/*.jar .

RUN ls 

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

RUN mv ./target/*.jar h3lp3r-back.jar

COPY --from=builder ${JARFILE} ./${JARFILE}

RUN pwd && ls -lh

ENTRYPOINT ["bash", "-c" ]
CMD ["/usr/bin/java -jar ${JARFILE} --port=${PORT} --elasticsearch-host=${ES_URL} --secret=${SECRET}"]
