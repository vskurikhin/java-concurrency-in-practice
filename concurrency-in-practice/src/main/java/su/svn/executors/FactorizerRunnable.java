package su.svn.executors;

import su.svn.Application;
import su.svn.console.ConsoleStub;
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

    static final BigInteger start = BigIntegerRandom.get();

    private final String URL = "http://localhost:" + Embedded.get().getPort() + "/go?n=";

    private final int sleep = FactorizerExecutor.START_POINT
            + new SecureRandom().nextInt(Environment.PAUSE_BEFORE_WARMUP_IN_MS);

    private String getNext() throws IOException, InterruptedException {
        String basicAuth = getBasicAuth();
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
        for (int i = 0; i < 9 && Application.Instance.getRootContext() == null; i++) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            ConsoleStub.println(this.getNext());
            FactorizerExecutor.Singleton.finish();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
