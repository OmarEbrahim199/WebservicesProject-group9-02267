package service.port;


import service.domain.Report;
import service.domain.ReportList;
import service.domain.ReportRequest;
import service.exception.IncorrectInformationException;

public interface IReportService {
    public void createUser(String userID);
    public Report createReport(ReportRequest request) throws IncorrectInformationException;
    public ReportList getManagerReports();
    public ReportList getReportsByID(String merchantID) throws IncorrectInformationException;

}
