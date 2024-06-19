package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        if (cache.containsKey(userId)) {
            return CompletableFuture.supplyAsync(() -> cache.get(userId), executor);
        }
        return CompletableFuture
                .supplyAsync(() -> new UserData(userId, "Details for " + userId), executor)
                .thenApply(userData -> {
                    setCache(userId, userData);
                    return userData;
                });
    }

    private void setCache(String id, UserData userData) {
        cache.put(id, userData);
    }
}
