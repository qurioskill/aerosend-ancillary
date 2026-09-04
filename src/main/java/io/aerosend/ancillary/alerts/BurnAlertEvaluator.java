package io.aerosend.ancillary.alerts;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates the five burn signals. Two or more breaches => AT_RISK, a bounce-rate breach
 * together with a hard-bounce breach => BURNED.
 */
@Component
public class BurnAlertEvaluator {

    public enum Signal { WARMUP_REPUTATION, INBOX_PLACEMENT, REPLY_RATE, BOUNCE_RATE, BOUNCE_TYPE }
    public enum Verdict { HEALTHY, AT_RISK, BURNED }
    public record Result(Verdict verdict, List<Signal> breaches) {}

    private final BurnAlertProperties limits;

    public BurnAlertEvaluator(BurnAlertProperties limits) { this.limits = limits; }

    public Result evaluate(DomainSnapshot s) {
        List<Signal> breaches = new ArrayList<>();
        if (s.warmupScore() != null && s.warmupScore() < limits.minWarmupScore()) breaches.add(Signal.WARMUP_REPUTATION);
        if (s.inboxPlacement() != null && s.inboxPlacement() < limits.minInboxPlacement()) breaches.add(Signal.INBOX_PLACEMENT);
        if (s.sent() > 0 && s.replyRate() < limits.minReplyRate()) breaches.add(Signal.REPLY_RATE);
        if (s.bounceRate() > limits.maxBounceRate()) breaches.add(Signal.BOUNCE_RATE);
        if (s.hardBounceShare() > limits.maxHardBounceShare()) breaches.add(Signal.BOUNCE_TYPE);

        Verdict verdict;
        if (breaches.contains(Signal.BOUNCE_RATE) && breaches.contains(Signal.BOUNCE_TYPE)) verdict = Verdict.BURNED;
        else if (breaches.size() >= 2) verdict = Verdict.AT_RISK;
        else verdict = Verdict.HEALTHY;
        return new Result(verdict, breaches);
    }
}
