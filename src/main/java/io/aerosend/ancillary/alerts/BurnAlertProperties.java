package io.aerosend.ancillary.alerts;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aerosend.burn-alerts")
public record BurnAlertProperties(double maxBounceRate, double minReplyRate, double minInboxPlacement,
                                  double minWarmupScore, double maxHardBounceShare) {}
