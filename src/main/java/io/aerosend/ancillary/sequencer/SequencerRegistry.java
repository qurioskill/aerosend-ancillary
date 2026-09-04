package io.aerosend.ancillary.sequencer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SequencerRegistry {

    private final Map<String, SequencerClient> byCode;

    public SequencerRegistry(List<SequencerClient> clients) {
        this.byCode = clients.stream().collect(Collectors.toMap(SequencerClient::code, Function.identity()));
    }

    public SequencerClient get(String code) {
        SequencerClient client = byCode.get(code);
        if (client == null) throw new IllegalArgumentException("Unsupported sequencer: " + code);
        return client;
    }
}
