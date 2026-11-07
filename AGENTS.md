# aerosend-ancillary

## Overview
Spring Boot service (Java 21, Maven artifact `io.aerosend:aerosend-ancillary`) that consumes platform events from the `aerosend.events` RabbitMQ exchange, reads PostgreSQL, calls third-party sequencer APIs, and serves the free tools over HTTP. Run tests with `mvn test` from this directory.

## Subdirectories & Boundaries
- src: [./src/AGENTS.md](./src/AGENTS.md) - All production code. The service packages are documented in [src/main/java/io/aerosend/ancillary/AGENTS.md](./src/main/java/io/aerosend/ancillary/AGENTS.md).
