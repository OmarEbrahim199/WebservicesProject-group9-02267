package facades.merchantFacade;

import facades.domain.*;
import facades.enums.UserType;
import facades.exceptions.RegistrationException;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class MerchantFacade {
    private MessageQueue queue;
    private CompletableFuture<String> future;
    private CompletableFuture<Payment> paymentFuture;
    private CompletableFuture<String> tokeConsumerFuture;
    private CompletableFuture<Account> getSpecificUserFuture;
    private CompletableFuture<Report> reportFuture;
    private CompletableFuture<MerchantReportList> reportRequested;
    private Payment p;

    public MerchantFacade(MessageQueue q) {
        queue = q;
        queue.addHandler("MerchantRegisteredSuccessfully", this::successfulMerchantRegistration);
        queue.addHandler("MerchantPaymentSuccessfully", this::successfulMerchantPayment);
        queue.addHandler("MerchantPaymentFailed", this::handleMerchantPaymentFailed);
        queue.addHandler("MerchantUnsuccessfulRegistration", this::unsuccessfulMerchantRegistration);
        queue.addHandler("UserID fetched", this::handleUserIdFetched);
        queue.addHandler("TokenNotFound", this::handleTokenNotFound);
        queue.addHandler("FoundSpecificUser", this::handleFoundSpecificUser);
        queue.addHandler("FoundSpecificUser", this::handleFoundSpecificUser);
        queue.addHandler("MerchantReportsSent", this::reportListReceived);
        queue.addHandler("MerchantReportsNotSent", this::reportListFailed);
    }
    public String registerMerchant(RegistrationDTO regInfo) {
        future = new CompletableFuture<>();
        Event tempE = new Event("UserRegister",  new Object[] { regInfo, UserType.MERCHANT});
        queue.publish(tempE);
        return future.join();
    }
    public void successfulMerchantRegistration(Event e) {
        var id = e.getArgument(0, String.class);
        future.complete(id);
    }
    private void unsuccessfulMerchantRegistration(Event e) {
        future.completeExceptionally(new RegistrationException(e.getArgument(0,String.class)));
    }

    public Payment paymentMerchant(Payment payment) {
        paymentFuture = new CompletableFuture<>();
        Event tempE = new Event("MerchantPaymentRequested",  new Object[] { payment });
        queue.publish(tempE);
        return paymentFuture.join();
    }

    public void successfulMerchantPayment(Event e) {
        var id = e.getArgument(0, Payment.class);
        paymentFuture.complete(id);
    }

    //Handles a unsuccesful payment
    private void handleMerchantPaymentFailed(Event e) {
        var id = e.getArgument(0, Payment.class);
        paymentFuture.complete(id);
    }

    //When initiating a payment it has to consume a token
    public String consumeToken(String tokenID){
        tokeConsumerFuture = new CompletableFuture<>();
        Event tempE = new Event("ConsumeTokenRequested",  new Object[] { tokenID });
        queue.publish(tempE);
        return tokeConsumerFuture.join();
    }

    //Handles a succesful consumeToken event
    private void handleUserIdFetched(Event event) {
        var id = event.getArgument(0,String.class);
        tokeConsumerFuture.complete(id);
    }

    //Handles an unsuccesful consumeToken event
    private void handleTokenNotFound(Event event) {
        var id = event.getArgument(0,String.class);
        tokeConsumerFuture.complete(id);
    }

    //Publishes a request for a specific user
    public Account getSpecificUser(String userID) {
        getSpecificUserFuture = new CompletableFuture<>();
        Event tempE = new Event("GetSpecificUserById",  new Object[] { userID });
        queue.publish(tempE);
        return getSpecificUserFuture.join();
    }

    //Handles a succesful event of fetching a specific user
    private void handleFoundSpecificUser(Event e) {
        var id = e.getArgument(0, Account.class);
        getSpecificUserFuture.complete(id);
    }

    //Publishes an event to createReport which gets initiated when a payment is wanted
    public void createReport(ReportRequest reportRequest){
        Event tempE = new Event("ReportCreationRequest",  new Object[] { reportRequest });
        queue.publish(tempE);
    }

    //Published an event to request a report
    public MerchantReportList reportListRequest(String userID) {
        reportRequested = new CompletableFuture<>();
        Event event = new Event("MerchantReportsRequest", new Object[] { userID });
        queue.publish(event);
        return reportRequested.join();
    }

    //Handles a succesful event of requesting report
    public void reportListReceived(Event event) {
        var list = event.getArgument(0, MerchantReportList.class);
        reportRequested.complete(list);
    }

    //Handles a unsuccesful event of requesting report
    public void reportListFailed(Event event) {
        reportRequested.complete(null);
    }

}
