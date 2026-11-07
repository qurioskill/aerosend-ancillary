# warmup

## Overview
HTTP client for reading mailbox warmup reputation scores from Warmupinbox.

## Interface
- `WarmupInboxClient.reputationScore(String apiKey, String email)`: returns the score as a `double`.

## Invariants
- The base URL `https://api.warmupinbox.com/v1` is set in code, not through `aerosend.*` configuration.
- The response must contain a numeric `score`, because the value is cast to `Number`.
- `WarmupInboxClient` has no caller in this repository, and no scheduled job exists, despite its Javadoc describing a pull for every active mailbox.

## Side Effects
- `GET /inboxes/{email}/score` to Warmupinbox, with the key in the `X-Api-Key` header.

## Verification
- Run: `mvn test` (from `repos/ancillary`). No test in this repository covers this package.
