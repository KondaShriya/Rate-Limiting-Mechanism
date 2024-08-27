package Rate.Limiting.Mechanism.project;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class RateLimitentity {
    @Id
    private String ip;
    private int requestCount;
    private Instant windowStart;
    private Instant blockedUntil;

    public RateLimitentity() {}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public Instant getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Instant windowStart) {
        this.windowStart = windowStart;
    }

    public Instant getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(Instant blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public RateLimitentity(String ip, int requestCount, Instant windowStart){
        this.ip = ip;
        this.requestCount = requestCount;
        this.windowStart = windowStart;
        this.blockedUntil = null;
    }
}
