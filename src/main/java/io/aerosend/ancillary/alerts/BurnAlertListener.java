package io.aerosend.ancillary.alerts;

import io.aerosend.ancillary.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BurnAlertListener {

    private final BurnAlertEvaluator evaluator;
    private final AlertNotifier notifier;

    public BurnAlertListener(BurnAlertEvaluator evaluator, AlertNotifier notifier) {
        this.evaluator = evaluator;
        this.notifier = notifier;
    }

    @RabbitListener(queues = RabbitConfig.BURN_ALERTS)
    public void onDailyRollup(DomainSnapshot snapshot) {
        var result = evaluator.evaluate(snapshot);
        if (result.verdict() != BurnAlertEvaluator.Verdict.HEALTHY) {
            notifier.notify(snapshot, result);
        }
    }
}
