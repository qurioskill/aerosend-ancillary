package io.aerosend.ancillary.webhooks;

import io.aerosend.ancillary.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Forwards every platform event to customer-registered webhook URLs (HMAC-signed). */
@Component
public class WebhookDispatcher {

    @RabbitListener(queues = RabbitConfig.WEBHOOKS)
    public void dispatch(Message message) {
        String eventType = message.getMessageProperties().getReceivedRoutingKey();
        // TODO(integrations): look up subscriptions for eventType, sign body, POST with retry/backoff
    }
}
