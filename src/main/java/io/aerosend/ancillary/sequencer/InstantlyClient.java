package io.aerosend.ancillary.sequencer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class InstantlyClient implements SequencerClient {

    private final RestClient http;

    public InstantlyClient(@Value("${aerosend.sequencers.instantly.base-url}") String baseUrl) {
        this.http = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override public String code() { return "INSTANTLY"; }

    @Override
    public String addMailbox(String apiKey, MailboxCredentials m) {
        Map<?, ?> res = http.post().uri("/accounts")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .body(Map.of("email", m.email(), "first_name", m.displayName(),
                         "provider_code", 1, "smtp_host", m.smtpHost(), "smtp_port", m.smtpPort(),
                         "imap_host", m.imapHost(), "imap_port", m.imapPort(),
                         "smtp_password", m.password(), "imap_password", m.password()))
            .retrieve().body(Map.class);
        return String.valueOf(res.get("id"));
    }

    @Override
    public void setDailyLimit(String apiKey, String id, int limit) {
        http.patch().uri("/accounts/{id}", id)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .body(Map.of("daily_limit", limit)).retrieve().toBodilessEntity();
    }
}
