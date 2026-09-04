package io.aerosend.ancillary.alerts;

/** Daily rollup for one domain, as published on deliverability.daily_rollup. */
public record DomainSnapshot(long domainId, String domain, long sent, long bounces, long hardBounces,
                             long replies, Double inboxPlacement, Double warmupScore) {
    public double bounceRate() { return sent == 0 ? 0 : (double) bounces / sent; }
    public double replyRate()  { return sent == 0 ? 0 : (double) replies / sent; }
    public double hardBounceShare() { return bounces == 0 ? 0 : (double) hardBounces / bounces; }
}
