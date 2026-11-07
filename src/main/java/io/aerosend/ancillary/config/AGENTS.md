# config

## Overview
Declares the RabbitMQ topology and JSON message conversion that the service's listeners depend on.

## Interface
- `RabbitConfig.MAILBOX_SYNC`: `q.ancillary.mailbox-sync`, bound with routing key `mailbox.created`.
- `RabbitConfig.BURN_ALERTS`: `q.ancillary.burn-alerts`, bound with routing key `deliverability.daily_rollup`.
- `RabbitConfig.WEBHOOKS`: `q.ancillary.webhooks`, bound with routing key `#`.
- Beans: `events` (`TopicExchange`), `mailboxSyncQueue`, `burnAlertsQueue`, `webhooksQueue`, `mailboxSync`, `burnAlerts`, `webhooks` (`Binding`), `jsonConverter` (`Jackson2JsonMessageConverter`).

## Invariants
- The exchange name comes from the `aerosend.events-exchange` property, which is `aerosend.events` in `application.yml`.
- All three queues are durable and dead-letter to `aerosend.dlx`.
- `aerosend.dlx` is only referenced here as a queue argument. No exchange of that name is declared in this repository.
- The `#` binding means `q.ancillary.webhooks` receives every event on the exchange, including those also routed to the other two queues.

## Side Effects
- Declares the exchange, queues, and bindings on the RabbitMQ broker through Spring AMQP.

## Verification
- Run: `mvn test` (from `repos/ancillary`). No test in this repository covers this package.
