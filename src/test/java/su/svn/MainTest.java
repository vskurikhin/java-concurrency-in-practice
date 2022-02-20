package su.svn;

import org.apache.catalina.LifecycleException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import su.svn.tomcat.Embedded;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MainTest {

    public static int PAUSE = 1500;

    public static void clearEmbedded() throws Exception {
        Class<Embedded> clazz = Embedded.class;

        Constructor<Embedded> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Embedded mock = constructor.newInstance();

        Field consoleOutputField = clazz.getDeclaredField("embedded");
        consoleOutputField.setAccessible(true);
        consoleOutputField.set(mock, null);
    }

    @Before
    public void setUp() throws Exception {
        clearEmbedded();
    }

    @Test
    public void main() throws Exception {
        new Thread(() -> {
            try {
                Thread.sleep(PAUSE);
                Embedded.get().stop();
            } catch (InterruptedException | LifecycleException e) {
                e.printStackTrace();
            }
        }).start();

        Main.main(new String[]{});
        new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/go"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/text")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        System.err.println(response.body());
    }
}