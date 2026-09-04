package io.aerosend.ancillary.sequencer;

import io.aerosend.ancillary.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Consumes mailbox.created (CDC from core's outbox) and pushes the mailbox to the customer's sequencer. */
@Component
public class MailboxSyncListener {

    public record MailboxCreated(long mailboxId, long domainId, String localPart) {}

    private static final Logger log = LoggerFactory.getLogger(MailboxSyncListener.class);

    private final MailboxLookup lookup;
    private final SequencerRegistry registry;

    public MailboxSyncListener(MailboxLookup lookup, SequencerRegistry registry) {
        this.lookup = lookup;
        this.registry = registry;
    }

    @RabbitListener(queues = RabbitConfig.MAILBOX_SYNC)
    public void onMailboxCreated(MailboxCreated event) {
        var target = lookup.syncTarget(event.mailboxId());
        if (target.isEmpty()) {
            log.info("Mailbox {} has no sequencer connected, skipping", event.mailboxId());
            return;
        }
        var t = target.get();
        String externalId = registry.get(t.sequencer()).addMailbox(t.apiKey(), t.credentials());
        log.info("Synced mailbox {} to {} as {}", event.mailboxId(), t.sequencer(), externalId);
    }
}
