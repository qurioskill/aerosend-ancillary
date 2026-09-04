package io.aerosend.ancillary.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MAILBOX_SYNC = "q.ancillary.mailbox-sync";
    public static final String BURN_ALERTS = "q.ancillary.burn-alerts";
    public static final String WEBHOOKS = "q.ancillary.webhooks";

    @Bean TopicExchange events(@Value("${aerosend.events-exchange}") String name) { return new TopicExchange(name); }

    @Bean Queue mailboxSyncQueue() { return QueueBuilder.durable(MAILBOX_SYNC).deadLetterExchange("aerosend.dlx").build(); }
    @Bean Queue burnAlertsQueue()  { return QueueBuilder.durable(BURN_ALERTS).deadLetterExchange("aerosend.dlx").build(); }
    @Bean Queue webhooksQueue()    { return QueueBuilder.durable(WEBHOOKS).deadLetterExchange("aerosend.dlx").build(); }

    @Bean Binding mailboxSync(TopicExchange events) { return BindingBuilder.bind(mailboxSyncQueue()).to(events).with("mailbox.created"); }
    @Bean Binding burnAlerts(TopicExchange events)  { return BindingBuilder.bind(burnAlertsQueue()).to(events).with("deliverability.daily_rollup"); }
    @Bean Binding webhooks(TopicExchange events)    { return BindingBuilder.bind(webhooksQueue()).to(events).with("#"); }

    @Bean Jackson2JsonMessageConverter jsonConverter() { return new Jackson2JsonMessageConverter(); }
}
