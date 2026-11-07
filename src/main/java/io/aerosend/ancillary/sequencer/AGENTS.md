# sequencer

## Overview
Pushes newly created mailboxes to the customer's third-party sequencer. A listener consumes `mailbox.created`, resolves the mailbox and customer integration from PostgreSQL, and calls the matching `SequencerClient`. Clients exist for Smartlead and Instantly.

## Interface
- `SequencerClient`: `code()`, `addMailbox(String customerApiKey, MailboxCredentials mailbox)`, `setDailyLimit(String customerApiKey, String sequencerMailboxId, int limit)`.
- `SequencerClient.MailboxCredentials`: record with `email`, `displayName`, `smtpHost`, `smtpPort`, `imapHost`, `imapPort`, `password`.
- `SmartleadClient`: code `SMARTLEAD`, base URL from `aerosend.sequencers.smartlead.base-url`.
- `InstantlyClient`: code `INSTANTLY`, base URL from `aerosend.sequencers.instantly.base-url`.
- `SequencerRegistry.get(String code)`: returns the client for a code.
- `MailboxLookup.syncTarget(long mailboxId)`: returns `Optional<MailboxLookup.SyncTarget>`, a record with `sequencer`, `apiKey`, `credentials`.
- `MailboxSyncListener.onMailboxCreated(MailboxCreated)`: `@RabbitListener` on `RabbitConfig.MAILBOX_SYNC`.
- `MailboxSyncListener.MailboxCreated`: event record with `mailboxId`, `domainId`, `localPart`.

## Invariants
- `SequencerRegistry` is built from every `SequencerClient` Spring bean, so a new `@Component` implementation registers itself. Its `code()` must match the value in `mailboxes.sequencer`.
- Two clients with the same `code()` fail registry construction, because `Collectors.toMap` is used without a merge function.
- `SequencerRegistry.get` throws `IllegalArgumentException` for an unknown code. `MailboxSyncListener` does not catch it.
- `MailboxLookup.syncTarget` uses inner joins, so a mailbox with no matching `customer_integrations` row returns empty, and the listener logs and skips it.
- `MailboxLookup` sets the SMTP host to `smtp.aerosend.io:587` and the IMAP host to `imap.aerosend.io:993` in code.
- Every `MailboxCredentials` field sent by a client must be non-null, because both clients build the request body with `Map.of`.
- `SmartleadClient` passes the API key as the `api_key` query parameter, and `InstantlyClient` sends it as a `Bearer` token.
- `addMailbox` returns `String.valueOf` of the response's `emailAccountId` (Smartlead) or `id` (Instantly), so a missing key yields the string `"null"`.
- `setDailyLimit` is part of the interface but has no caller in this repository.
- As of this writing, `mailbox.created` is emitted only by `DomainProvisioningService.createMailboxes` in `repos/core`, which has no caller. `customer_integrations` has no DDL in any repo.

## Side Effects
- Consumes messages from `q.ancillary.mailbox-sync`.
- Reads `mailboxes`, `domains`, and `customer_integrations` through `JdbcTemplate`. This package issues no writes.
- Smartlead HTTP calls: `POST /email-accounts/save`, `POST /email-accounts/{id}`.
- Instantly HTTP calls: `POST /accounts`, `PATCH /accounts/{id}`.
- Sends the mailbox password to the sequencer. `InstantlyClient` uses the same value for `smtp_password` and `imap_password`.
- The sequencer's external id is only logged by `MailboxSyncListener`. It is not stored.

## Verification
- Run: `mvn test` (from `repos/ancillary`). No test in this repository covers this package.
