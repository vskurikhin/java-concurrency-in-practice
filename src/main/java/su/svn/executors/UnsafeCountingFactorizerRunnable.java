package su.svn.executors;

import su.svn.console.ConsoleStub;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

class UnsafeCountingFactorizerRunnable implements Runnable {

    private int getNext() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/go"))
                .timeout(Duration.ofSeconds(1))
                .expectContinue(false)
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(500))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Integer.parseInt(response.body().trim());
    }

    @Override
    public void run() {
        try {
            ConsoleStub.print(" " + this.getNext());
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
