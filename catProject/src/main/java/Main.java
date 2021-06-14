/**
 * Запрос на получение списка фактов о кошках
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static final String REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        //создаем http-клиента
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(3000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        //создаем объект запроса
        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        //отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println("Вывод списка тех фактов, у которых поле upvotes не равно null");
        List<Cats> catsListJSON = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<Cats>>() {
        });
        Stream<Cats> stream = catsListJSON.stream();
        stream.filter(value -> value.getUpvotes() != (Integer) null && value.getUpvotes() > 0)
                .forEach(System.out::println);
    }
}
