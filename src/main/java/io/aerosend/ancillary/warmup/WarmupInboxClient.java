package io.aerosend.ancillary.warmup;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/** Pulls warmup reputation scores from Warmupinbox.com for every active mailbox. */
@Component
public class WarmupInboxClient {

    private final RestClient http = RestClient.create("https://api.warmupinbox.com/v1");

    public double reputationScore(String apiKey, String email) {
        Map<?, ?> res = http.get().uri("/inboxes/{email}/score", email)
            .header("X-Api-Key", apiKey)
            .retrieve().body(Map.class);
        return ((Number) res.get("score")).doubleValue();
    }
}
