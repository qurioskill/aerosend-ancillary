# webhooks

## Overview
Entry point for forwarding platform events to customer webhooks. It is currently a stub: it consumes events and sends nothing.

## Interface
- `WebhookDispatcher.dispatch(Message)`: `@RabbitListener` on `RabbitConfig.WEBHOOKS`.

## Invariants
- `dispatch` reads the event type from `getReceivedRoutingKey()` and then returns. The subscription lookup, signing, and retrying POST in its Javadoc and `TODO(integrations)` are not implemented.
- Do not assume customer webhooks are delivered.

## Side Effects
- Consumes every event routed to `q.ancillary.webhooks`, which is bound with `#`. Those messages are currently discarded after `dispatch` returns.

## Verification
- Run: `mvn test` (from `repos/ancillary`). No test in this repository covers this package.
