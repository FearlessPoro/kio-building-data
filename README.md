# Kio Building Data

This is a Spring Boot application developed in Java. It's designed to be a proxy service for Kontakt.io API and allows
users to retrieve building data from Kontakt.io API in a much simpler way. This project assumes authentication is
already implemented.

## Getting Started

### Prerequisites

- Java 11
- Gradle

### Installing

1. Clone the repository
2. Navigate to the project directory
3. Run `gradle bootRun` to start the application

## Running the tests

To run the tests, use the command `gradle test`.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Gradle](https://gradle.org/)

## Additional work

While outside the scope of this project, it would most likely be required to add some sort of DB layer to
store the data to make it more functional. Currently, it only allows the user to retrieve data from Kontakt.io API and
read it.

If such a feature were to be added, it would be recommended to use a database that would allow for easy storage of
spatial data in the form of geoJson images. One such database is PostgreSQL with the PostGIS extension, which are
designed to handle geospatial data efficiently.

Another idea would be to use mongoDB, a noSQL database that is designed to handle JSON-like documents.
