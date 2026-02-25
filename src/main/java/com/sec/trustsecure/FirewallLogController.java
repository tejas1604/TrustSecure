package com.sec.trustsecure;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirewallLogController {

    private final TrustsecureApplication trustsecureapplication;

    public FirewallLogController(TrustsecureApplication trustsecureapplication) {
        this.trustsecureapplication = trustsecureapplication;
    }

    @GetMapping("/firewall-log")
    public String getFirewallLog() {
        return trustsecureapplication.readReadableFirewallLog();
    }
}
