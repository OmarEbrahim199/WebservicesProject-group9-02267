package service.port;

import service.domain.Report;
import service.domain.ReportList;
import service.exception.IncorrectInformationException;

import java.util.ArrayList;

public interface IReportRepository {
    ArrayList<ReportList> getReports();
    ReportList getReportsByUser(String userID) throws IncorrectInformationException;
    Report getReportByPaymentID(String paymentID) throws IncorrectInformationException;
    ReportList createReportList(String userID);
    ReportList addReport(String userID, Report newReport) throws IncorrectInformationException;
}
