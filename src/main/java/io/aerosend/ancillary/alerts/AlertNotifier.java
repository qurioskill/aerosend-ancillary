package io.aerosend.ancillary.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Sends burn alerts to the customer (email + optional Slack webhook). */
@Component
public class AlertNotifier {

    private static final Logger log = LoggerFactory.getLogger(AlertNotifier.class);

    public void notify(DomainSnapshot snapshot, BurnAlertEvaluator.Result result) {
        // TODO(integrations): wire to SES + customer Slack webhook
        log.warn("Domain {} is {} ({})", snapshot.domain(), result.verdict(), result.breaches());
    }
}
