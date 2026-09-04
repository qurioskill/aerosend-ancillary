package io.aerosend.ancillary.tools;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainVariationsGeneratorTest {

    @Test
    void neverReturnsThePrimaryDomain() {
        var out = new DomainVariationsGenerator().variations("acme.com", 50);
        assertThat(out).doesNotContain("acme.com").contains("getacme.com", "acmehq.com");
    }
}
