package su.svn.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import su.svn.Application;
import su.svn.enums.Environment;
import su.svn.tomcat.Embedded;
import su.svn.utils.BigIntegerRandom;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

class FactorizerRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactorizerRunnable.class);

    static final BigInteger start = BigIntegerRandom.get();

    private final String URL = "http://localhost:" + Embedded.get().getPort() + "/go?n=";

    private final int sleep = FactorizerExecutor.START_POINT
            + new SecureRandom().nextInt(Environment.PAUSE_BEFORE_WARMUP_IN_MS);

    private String getNext() throws IOException, InterruptedException {
        String basicAuth = getBasicAuth();
        LOGGER.info("getBasicAuth {}", basicAuth);
        String url = URL + add();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("Authorization", "Basic " + basicAuth)
                .timeout(Duration.ofSeconds(60 * FactorizerExecutor.MAX_BOUND))
                .expectContinue(false)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(500))
                .build();
        Thread.sleep(sleep);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String getBasicAuth() {
        ApplicationContext rootContext = Application.Instance.getRootContext();
        for (int i = 0; i < 9 && rootContext == null; i++) {
            rootContext = Application.Instance.getRootContext();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                LOGGER.error("sleep ", e);
            }
        }
        Object o = Application.Instance.getRootContext().getBean("users");
        if (o instanceof Map) {
            //noinspection unchecked
            Map<String, Object> users = (Map<String, Object>) o;
            String password = users.get("user").toString();
            return Base64.getEncoder().encodeToString(("user:" + password).getBytes());
        }
        return null;
    }

    private BigInteger add() {
        synchronized (start){
            return start.add(new BigInteger(Integer.toString(new Random().nextInt(FactorizerExecutor.MAX_BOUND))));
        }
    }

    @Override
    public void run() {
        try {
            this.getNext();
            FactorizerExecutor.Singleton.finish();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
