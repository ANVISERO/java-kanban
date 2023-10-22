package main.ru.yandex.practicum.kanban;


import main.ru.yandex.practicum.kanban.service.server.HttpTaskServer;
import main.ru.yandex.practicum.kanban.service.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer().startHttpTaskServer();
        } catch (IOException e) {
            System.out.println("Ошибка при запуске");
        }
    }
}
