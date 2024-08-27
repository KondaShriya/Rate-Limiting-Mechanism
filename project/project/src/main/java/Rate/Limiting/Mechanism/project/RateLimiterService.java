package Rate.Limiting.Mechanism.project;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS = 3;
    private static final long TIME_WINDOW = TimeUnit.SECONDS.toMillis(10);
    private static final long BLOCK_TIME = TimeUnit.MINUTES.toMillis(1);

    private final ConcurrentHashMap<String, RequestInfo> ipRequestMap = new ConcurrentHashMap<>();

    public boolean isBlocked(String ip) {
        RequestInfo requestInfo = ipRequestMap.get(ip);

        if (requestInfo == null) {
            requestInfo = new RequestInfo(0, Instant.now().toEpochMilli(), false);
            ipRequestMap.put(ip, requestInfo);
        }

        long currentTime = Instant.now().toEpochMilli();

        if (requestInfo.isBlocked) {
            if (currentTime - requestInfo.blockStartTime >= BLOCK_TIME) {
                // Unblock the IP after BLOCK_TIME
                requestInfo.isBlocked = false;
                requestInfo.requestCount = 0;
                requestInfo.startTime = currentTime;
            } else {
                return true; // IP is still blocked
            }
        }

        if (currentTime - requestInfo.startTime < TIME_WINDOW) {
            if (requestInfo.requestCount < MAX_REQUESTS) {
                requestInfo.requestCount++;
            } else {
                requestInfo.isBlocked = true;
                requestInfo.blockStartTime = currentTime;
                return true; // IP is blocked due to too many requests
            }
        } else {
            // Reset counter and start time if TIME_WINDOW has passed
            requestInfo.requestCount = 1;
            requestInfo.startTime = currentTime;
        }

        return false; // IP is not blocked
    }

    private static class RequestInfo {
        int requestCount;
        long startTime;
        boolean isBlocked;
        long blockStartTime;

        public RequestInfo(int requestCount, long startTime, boolean isBlocked) {
            this.requestCount = requestCount;
            this.startTime = startTime;
            this.isBlocked = isBlocked;
        }
    }
}
