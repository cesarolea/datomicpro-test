FROM amazoncorretto:17

# Download datomic distribution
RUN curl https://datomic-pro-downloads.s3.amazonaws.com/1.0.6735/datomic-pro-1.0.6735.zip -O

RUN yum install -y unzip

# Extract
RUN unzip datomic-pro-1.0.6735.zip -d /opt \
  && rm datomic-pro-1.0.6735.zip \
  && mv /opt/datomic-pro-1.0.6735 /opt/datomic-pro

# Config
# RUN cp /opt/datomic-pro/config/samples/dev-transactor-template.properties /opt/datomic-pro/config/dev-transactor.properties

WORKDIR /opt/datomic-pro

EXPOSE 4334 4335 4336

CMD ["bin/transactor", "/opt/datomic-pro/config/dev-transactor.properties"]
