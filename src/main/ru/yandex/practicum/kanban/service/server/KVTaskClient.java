package main.ru.yandex.practicum.kanban.service.server;

import main.ru.yandex.practicum.kanban.service.exceptions.KVTaskClientException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String KV_SERVER_URI;

    private String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(final String uri) {
        this.httpClient = HttpClient.newHttpClient();
        this.KV_SERVER_URI = uri;
        register();
    }

    public void put(String key, String json) {
        try {
            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(URI.create(KV_SERVER_URI + "save/" + key + "?API_TOKEN=" + apiToken))
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<Void> putResponse = httpClient.send(putRequest, HttpResponse.BodyHandlers.discarding());
            if (putResponse.statusCode() != 200) {
                throw new KVTaskClientException("Не удалось сохранить состояние менеджера задач на KVServer.\n" +
                        "Код ответа: " + putResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка во время сохранения состояния менеджера задач на KVServer.");
        } catch (IllegalArgumentException e) {
            throw new KVTaskClientException("Введённый вами адрес не соответствует формату URL.");
        }
    }

    // Возвращает состояние менеджера из KVServer
    public String load(String key) {
        try {
            HttpRequest loadRequest = HttpRequest.newBuilder()
                    .uri(URI.create(KV_SERVER_URI + "load/" + key + "?API_TOKEN=" + apiToken))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();
            HttpResponse<String> loadResponse
                    = httpClient.send(loadRequest, HttpResponse.BodyHandlers.ofString());
            if (loadResponse.statusCode() == 200) {
                return loadResponse.body();
            } else {
                throw new KVTaskClientException("Данные от KVServer не получены.\n" +
                        "Код ответа: " + loadResponse.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка во время возвращения состояния менеджера из KVServer.");
        } catch (IllegalArgumentException e) {
            throw new KVTaskClientException("Введённый вами адрес не соответствует формату URL.");
        }
    }

    //  Регистрирует клиента и выдаёт уникальный токен доступа (аутентификации)
    public void register() {
        try {
            HttpRequest registerRequest = HttpRequest.newBuilder()
                    .uri(URI.create(KV_SERVER_URI + "register"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();
            HttpResponse<String> registerResponse
                    = httpClient.send(registerRequest, HttpResponse.BodyHandlers.ofString());
            if (registerResponse.statusCode() == 200) {
                apiToken = registerResponse.body();
            } else {
                throw new KVTaskClientException("Токен от KVServer не получен.\n" +
                        "Код ответа: " + registerResponse.statusCode());
            }
            System.out.println("API_TOKEN : " + apiToken);
        } catch (IOException | InterruptedException e) {
            throw new KVTaskClientException("Ошибка во время регистрации клиента на KVServer.");
        } catch (IllegalArgumentException e) {
            throw new KVTaskClientException("Введённый вами адрес не соответствует формату URL.");
        }
    }

}
