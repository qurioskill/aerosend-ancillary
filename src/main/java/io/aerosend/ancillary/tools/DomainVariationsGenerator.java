package io.aerosend.ancillary.tools;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Free tool: suggests look-alike sending domains so cold email never touches the primary domain. */
@Component
public class DomainVariationsGenerator {

    private static final List<String> PREFIXES = List.of("get", "try", "use", "hello", "meet", "go");
    private static final List<String> SUFFIXES = List.of("hq", "app", "team", "mail", "io");
    private static final List<String> TLDS = List.of(".com", ".co", ".io");

    public List<String> variations(String primaryDomain, int limit) {
        String base = primaryDomain.toLowerCase().replaceFirst("^www\\.", "").replaceFirst("\\.[a-z.]+$", "");
        Set<String> out = new LinkedHashSet<>();
        for (String tld : TLDS) {
            for (String p : PREFIXES) out.add(p + base + tld);
            for (String s : SUFFIXES) out.add(base + s + tld);
        }
        out.remove(primaryDomain.toLowerCase());
        return out.stream().limit(limit).toList();
    }
}
