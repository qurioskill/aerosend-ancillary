package io.aerosend.ancillary.sequencer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/** Reads mailbox + customer integration settings from the core read replica. */
@Component
public class MailboxLookup {

    public record SyncTarget(String sequencer, String apiKey, SequencerClient.MailboxCredentials credentials) {}

    private final JdbcTemplate jdbc;

    public MailboxLookup(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public Optional<SyncTarget> syncTarget(long mailboxId) {
        return jdbc.query("""
            SELECT m.sequencer, ci.api_key, m.local_part || '@' || d.name, m.display_name, ci.smtp_password
            FROM mailboxes m
            JOIN domains d ON d.id = m.domain_id
            JOIN customer_integrations ci ON ci.customer_id = d.customer_id AND ci.sequencer = m.sequencer
            WHERE m.id = ?
            """,
            (rs, i) -> new SyncTarget(rs.getString(1), rs.getString(2),
                new SequencerClient.MailboxCredentials(rs.getString(3), rs.getString(4),
                    "smtp.aerosend.io", 587, "imap.aerosend.io", 993, rs.getString(5))),
            mailboxId).stream().findFirst();
    }
}
