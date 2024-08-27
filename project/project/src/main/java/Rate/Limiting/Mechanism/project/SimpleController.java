package Rate.Limiting.Mechanism.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    private final RateLimiterService rateLimiterService;

    @Autowired
    public SimpleController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(@RequestHeader(value = "X-FORWARDED-FOR", required = false) String ip) {
        if (ip == null || ip.isEmpty()) {
            ip = "127.0.0.1"; // Default to localhost for simplicity
        }

        if (rateLimiterService.isBlocked(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Rate limit exceeded. Please try again later.");
        }

        return ResponseEntity.ok("Status OK");
    }
}

