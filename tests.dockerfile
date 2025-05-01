FROM maven
WORKDIR /app
COPY . /app

RUN rm src/test/resources/application.yml
RUN cp src/test/resources/application.example.yml src/test/resources/application.yml

ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

CMD ./mvnw test
