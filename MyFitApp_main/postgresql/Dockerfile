FROM postgres:15.1-alpine
MAINTAINER ITAcademy

ADD /audit-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /product-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /report-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /user-service/ddl/*.sql /docker-entrypoint-initdb.d/
