# ancillary

## Overview
Root package of the `aerosend-ancillary` Spring Boot service. Holds the application entry point and groups the event-driven integrations and free tools.

## Interface
- `AncillaryApplication.main(String[])`: starts the service. Annotated `@SpringBootApplication` and `@ConfigurationPropertiesScan`.
- [alerts](./alerts/AGENTS.md): burn signal evaluation and notification.
- [config](./config/AGENTS.md): RabbitMQ topology and message conversion.
- [sequencer](./sequencer/AGENTS.md): mailbox sync to Smartlead and Instantly.
- [tools](./tools/AGENTS.md): `GET /tools/domain-variations`.
- [warmup](./warmup/AGENTS.md): Warmupinbox score client.
- [webhooks](./webhooks/AGENTS.md): stub event consumer for customer webhooks.

## Invariants
- `@ConfigurationPropertiesScan` is what registers `alerts.BurnAlertProperties`, which has no `@Component` or `@EnableConfigurationProperties` of its own.
- Component scanning starts at `io.aerosend.ancillary`, so beans must live in this package or below it.

## Side Effects
- Startup creates the Spring context, including the RabbitMQ listeners and the embedded web server on port 8082 (`server.port` in `application.yml`).

## Verification
- Run: `mvn test` (from `repos/ancillary`)
