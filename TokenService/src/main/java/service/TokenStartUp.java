package service;

import messaging.implementations.RabbitMqQueue;
import service.adapter.TokenAdapter;
import service.storage.TokenRepository;

public class TokenStartUp {
    public static void main(String[] args) throws Exception {
        new TokenStartUp().startUp();
    }

    private void startUp() throws Exception {
        System.out.println("startup");
//        var mq = new RabbitMqQueue("localhost");
        var mq = new RabbitMqQueue("rabbitmq");
        var service = new TokenService(new TokenRepository());
        new TokenAdapter(mq, service);
    }
}
