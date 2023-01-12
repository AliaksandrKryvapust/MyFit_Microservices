FROM postgres:15.1-alpine
MAINTAINER aliaksandr_kryvapust

ADD /audit-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /product_and_user-service/ddl/*.sql /docker-entrypoint-initdb.d/
