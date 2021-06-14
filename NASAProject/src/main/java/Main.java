/**
 * Чтение данных API NASA
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

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(3000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        System.out.println("Запрос на сайт NASA...");
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=vEMDIz35GO97IqIvimAluRPnpJY1uFRqmjvVqOSz");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        NasaJSON nasaJSONObject = mapper.readValue(response.getEntity().getContent(), new TypeReference<NasaJSON>() {
        });
        System.out.println("Запрос на получение JPG...");
        HttpGet requestURL = new HttpGet(nasaJSONObject.getUrl());
        requestURL.setHeader(HttpHeaders.ACCEPT, ContentType.IMAGE_JPEG.getMimeType());
        CloseableHttpResponse responseURL = httpClient.execute(requestURL);
        byte[] buffer = responseURL.getEntity().getContent().readAllBytes();
        File fileNASA = new File(nasaJSONObject.getUrl());
        try (FileOutputStream fos = new FileOutputStream(fileNASA.getName());
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            System.out.println("Запись фала...");
            bos.write(buffer, 0, buffer.length);
            System.out.println("Файл " + fileNASA.getName() + " записан");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
