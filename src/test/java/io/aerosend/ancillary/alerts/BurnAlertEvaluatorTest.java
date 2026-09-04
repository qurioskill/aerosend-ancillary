package io.aerosend.ancillary.alerts;

import org.junit.jupiter.api.Test;

import static io.aerosend.ancillary.alerts.BurnAlertEvaluator.Signal.*;
import static io.aerosend.ancillary.alerts.BurnAlertEvaluator.Verdict.*;
import static org.assertj.core.api.Assertions.assertThat;

class BurnAlertEvaluatorTest {

    private final BurnAlertEvaluator evaluator =
        new BurnAlertEvaluator(new BurnAlertProperties(0.03, 0.005, 0.70, 85, 0.5));

    @Test
    void healthyDomain() {
        var r = evaluator.evaluate(new DomainSnapshot(1, "getacme.com", 1000, 10, 2, 12, 0.92, 97.0));
        assertThat(r.verdict()).isEqualTo(HEALTHY);
        assertThat(r.breaches()).isEmpty();
    }

    @Test
    void highHardBouncesBurnTheDomain() {
        var r = evaluator.evaluate(new DomainSnapshot(1, "getacme.com", 1000, 80, 60, 12, 0.92, 97.0));
        assertThat(r.verdict()).isEqualTo(BURNED);
        assertThat(r.breaches()).containsExactly(BOUNCE_RATE, BOUNCE_TYPE);
    }

    @Test
    void lowPlacementAndWarmupIsAtRisk() {
        var r = evaluator.evaluate(new DomainSnapshot(1, "getacme.com", 1000, 10, 2, 12, 0.55, 70.0));
        assertThat(r.verdict()).isEqualTo(AT_RISK);
    }
}
