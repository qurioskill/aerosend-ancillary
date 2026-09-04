# aerosend-ancillary

Everything around the core that changes often: **sequencer integrations, domain burn alerts, warmup sync, outbound webhooks, and the free marketing tools** (Domain Variations Generator, Deliverability Checker).

| | |
|---|---|
| Category | ancillary |
| Change frequency | **high** (several deploys a week; sequencer APIs change under us) |
| Stack | Java 21, Spring Boot 3, Spring AMQP (RabbitMQ), PostgreSQL (read-only replica) |
| Owners | Integrations team |

## Event flow

```
aerosend-core ──(outbox row)──► Debezium CDC ──► RabbitMQ exchange "aerosend.events"
                                                     │
          ┌──────────────────────────────────────────┼──────────────────────────┐
          ▼                                          ▼                          ▼
 q.ancillary.mailbox-sync               q.ancillary.burn-alerts       q.ancillary.webhooks
 (push new mailboxes to sequencers)     (evaluate 5 burn metrics)     (customer webhooks)
```

## Sequencers supported

Smartlead, Instantly, EmailBison, lemlist, Saleshandy, Woodpecker. Each implements `sequencer.SequencerClient`.
Adding a new sequencer = new client class + entry in `SequencerRegistry` + config under `aerosend.sequencers.*`.

## Burn alert metrics

Warmup reputation, inbox placement, reply rate by domain, bounce rate by domain, bounce type. See `alerts/BurnAlertEvaluator`.

## Run locally

Start infra from `aerosend-core` (`docker compose up -d`), then `mvn spring-boot:run`.
