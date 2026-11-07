# tools

## Overview
Backs the free marketing tools. Currently exposes the Domain Variations Generator, which suggests look-alike sending domains over HTTP.

## Interface
- `GET /tools/domain-variations`: `FreeToolsController.domainVariations(@RequestParam String domain, @RequestParam(defaultValue = "20") int limit)`, returns `List<String>`.
- `DomainVariationsGenerator.variations(String primaryDomain, int limit)`: returns `List<String>`.

## Invariants
- `FreeToolsController` caps `limit` at 50 with `Math.min(limit, 50)`. `DomainVariationsGenerator` applies no cap of its own.
- A negative `limit` is not rejected by the controller, and reaches `Stream.limit`, which throws `IllegalArgumentException`.
- The result never contains the lowercased `primaryDomain`.
- The base name is the lowercased input with any leading `www.` removed, then everything from its first dot onward removed, so `www.foo.co.uk` gives `foo`.
- Output order is deterministic: for each of `.com`, `.co`, `.io`, all `PREFIXES` entries and then all `SUFFIXES` entries.

## Side Effects
- None. Generation is in-memory, with no network, database, or disk access.

## Verification
- Run: `mvn test` (from `repos/ancillary`). Covered by `DomainVariationsGeneratorTest`.
