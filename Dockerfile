FROM openjdk:latest
COPY ./target/inventory-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
EXPOSE 5432
ENTRYPOINT ["java"]
CMD ["-jar", "-DINVENTORY_POSTGRES_URL=localhost", "inventory-0.0.1-SNAPSHOT.jar"]
