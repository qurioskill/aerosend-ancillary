# alerts

## Overview
Evaluates a daily per-domain deliverability rollup against five burn signals and notifies when a domain is not healthy. Consumes `deliverability.daily_rollup` events from `q.ancillary.burn-alerts`.

## Interface
- `BurnAlertListener.onDailyRollup(DomainSnapshot)`: `@RabbitListener` on `RabbitConfig.BURN_ALERTS`.
- `BurnAlertEvaluator.evaluate(DomainSnapshot)`: returns `BurnAlertEvaluator.Result(Verdict verdict, List<Signal> breaches)`.
- `BurnAlertEvaluator.Signal`: `WARMUP_REPUTATION`, `INBOX_PLACEMENT`, `REPLY_RATE`, `BOUNCE_RATE`, `BOUNCE_TYPE`.
- `BurnAlertEvaluator.Verdict`: `HEALTHY`, `AT_RISK`, `BURNED`.
- `AlertNotifier.notify(DomainSnapshot, BurnAlertEvaluator.Result)`.
- `BurnAlertProperties`: `@ConfigurationProperties("aerosend.burn-alerts")` record with `maxBounceRate`, `minReplyRate`, `minInboxPlacement`, `minWarmupScore`, `maxHardBounceShare`.
- `DomainSnapshot`: event payload record with `domainId`, `domain`, `sent`, `bounces`, `hardBounces`, `replies`, `inboxPlacement`, `warmupScore`, and derived `bounceRate()`, `replyRate()`, `hardBounceShare()`.

## Invariants
- `BURNED` requires both `BOUNCE_RATE` and `BOUNCE_TYPE` breaches, and takes precedence over `AT_RISK` (`BurnAlertEvaluator.evaluate`).
- `AT_RISK` requires two or more breaches. A single breach is `HEALTHY`, although `Result.breaches` still lists it.
- `BurnAlertListener` calls `AlertNotifier` only when the verdict is not `HEALTHY`.
- A null `warmupScore` or `inboxPlacement` means that signal is skipped, not breached.
- `REPLY_RATE` is evaluated only when `sent > 0`.
- The derived rates return 0 instead of dividing by zero: `bounceRate()` and `replyRate()` when `sent == 0`, `hardBounceShare()` when `bounces == 0`.
- Minimum thresholds breach on strictly less than the limit, and maximum thresholds on strictly greater than the limit.
- As of this writing, nothing in `repos/core` or `repos/utils` emits `deliverability.daily_rollup`, so this listener receives no events.

## Side Effects
- Consumes messages from `q.ancillary.burn-alerts`.
- `AlertNotifier.notify` only writes a WARN log line. The email and Slack delivery in its Javadoc is not implemented (`TODO(integrations)`).

## Verification
- Run: `mvn test` (from `repos/ancillary`). Covered by `BurnAlertEvaluatorTest`.
