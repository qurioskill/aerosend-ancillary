package io.aerosend.ancillary.sequencer;

/** One implementation per third-party sequencer (Smartlead, Instantly, ...). */
public interface SequencerClient {

    record MailboxCredentials(String email, String displayName, String smtpHost, int smtpPort,
                              String imapHost, int imapPort, String password) {}

    String code();

    /** Registers the mailbox with the customer's sequencer account. Returns the sequencer's id for it. */
    String addMailbox(String customerApiKey, MailboxCredentials mailbox);

    void setDailyLimit(String customerApiKey, String sequencerMailboxId, int limit);
}
