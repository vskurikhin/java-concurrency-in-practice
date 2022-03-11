package su.svn.executors;

import su.svn.console.ConsoleStub;
import su.svn.utils.BigIntegerRandom;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Random;

class FactorizerRunnable implements Runnable {

    static final BigInteger start = BigIntegerRandom.get();

    private String getNext() throws IOException, InterruptedException {
        String url = "http://localhost:8080/go?n=" + add();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(60 * FactorizerExecutor.MAX_BOUND))
                .expectContinue(false)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(500))
                .build();
        Thread.sleep(FactorizerExecutor.START_POINT + new Random().nextInt(FactorizerExecutor.SLEEP_BEFORE_GET));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
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
            FactorizerExecutor.get().finish();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
