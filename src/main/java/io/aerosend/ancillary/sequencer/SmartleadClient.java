package io.aerosend.ancillary.sequencer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class SmartleadClient implements SequencerClient {

    private final RestClient http;

    public SmartleadClient(@Value("${aerosend.sequencers.smartlead.base-url}") String baseUrl) {
        this.http = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override public String code() { return "SMARTLEAD"; }

    @Override
    public String addMailbox(String apiKey, MailboxCredentials m) {
        Map<?, ?> res = http.post().uri("/email-accounts/save?api_key={k}", apiKey)
            .body(Map.of("from_email", m.email(), "from_name", m.displayName(),
                         "smtp_host", m.smtpHost(), "smtp_port", m.smtpPort(),
                         "imap_host", m.imapHost(), "imap_port", m.imapPort(),
                         "username", m.email(), "password", m.password()))
            .retrieve().body(Map.class);
        return String.valueOf(res.get("emailAccountId"));
    }

    @Override
    public void setDailyLimit(String apiKey, String id, int limit) {
        http.post().uri("/email-accounts/{id}?api_key={k}", id, apiKey)
            .body(Map.of("max_email_per_day", limit)).retrieve().toBodilessEntity();
    }
}
