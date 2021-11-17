FROM openjdk:latest
COPY ./target/inventory-0.0.1-SNAPSHOT.jar ./
ENV INVENTORY_POSTGRES_HOST 172.17.0.2
ENTRYPOINT ["java"]
CMD ["-jar", "inventory-0.0.1-SNAPSHOT.jar"]
