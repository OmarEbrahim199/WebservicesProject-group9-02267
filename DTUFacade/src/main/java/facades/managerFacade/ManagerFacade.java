package facades.managerFacade;

import facades.domain.ReportList;
import messaging.Event;
import messaging.MessageQueue;

import java.util.concurrent.CompletableFuture;

public class ManagerFacade {
    private MessageQueue queue;
    private CompletableFuture<String> future;
    private CompletableFuture<ReportList> reportRequested;
    public ManagerFacade(MessageQueue q) {
        queue = q;
        queue.addHandler("ManagerReportRequest", this::succesfulReportRequested);
    }

    private void succesfulReportRequested(Event e) {
        var report = e.getArgument(0, ReportList.class);
        reportRequested.complete(report);
    }

    //Initiates an event to get all lists from ReportService
    public ReportList reportListRecived() {
        reportRequested = new CompletableFuture<>();
        Event event = new Event("ReportManager", new Object[] {  });
        queue.publish(event);
        return reportRequested.join();
    }

}
