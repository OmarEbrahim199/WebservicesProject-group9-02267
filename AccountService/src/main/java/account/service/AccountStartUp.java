package account.service;
import account.service.adapter.AccountAdapter;
import account.service.repository.AccountRepositoryAdapter;
import messaging.implementations.RabbitMqQueue;

public class AccountStartUp {
    public static void main(String[] args) throws Exception {
            new AccountStartUp().startUp();
        }

        private void startUp() throws Exception {
            System.out.println("startup");
            var mq = new RabbitMqQueue("rabbitmq");
            new AccountAdapter(mq, new AccountService(new AccountRepositoryAdapter()));
        }
}
