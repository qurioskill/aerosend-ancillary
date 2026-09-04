package io.aerosend.ancillary.tools;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class FreeToolsController {

    private final DomainVariationsGenerator variations;

    public FreeToolsController(DomainVariationsGenerator variations) { this.variations = variations; }

    @GetMapping("/domain-variations")
    public List<String> domainVariations(@RequestParam String domain,
                                         @RequestParam(defaultValue = "20") int limit) {
        return variations.variations(domain, Math.min(limit, 50));
    }
}
