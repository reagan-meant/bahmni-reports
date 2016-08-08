package org.bahmni.reports.report;

import org.bahmni.reports.report.integrationtests.BaseIntegrationTest;
import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AggregationReportTest extends BaseIntegrationTest {
    public AggregationReportTest() {
        super("src/test/resources/config/aggregationReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");
    }

    @Test
    public void shouldGivePatientCountOnGroupByAgeAndGender() throws Exception {
        String reportName = "Aggregate Report Name";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("1 1", report.getRowAsString(3, " "));
    }


    @Test
    public void shouldGiveOnlyOnePatientOnGroupByAgeAndGenderForObsReportWhenConceptNameFilterIsAppliedInConfig() throws Exception {
        String reportName = "Aggregate Report with config";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Weight"))).thenReturn(objectList.toString());


        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("1", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldGivePatientOnGroupByAgeAndPatientIdAsRowGroupAndGenderAsColumnGroupForObsReport() throws Exception {
        String reportName = "Aggregate Report with multiple Row Groups config";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(4, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("1000 1 0", report.getRowAsString(3, " "));
        assertEquals("1001 0 1", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldGivePatientOnGroupByAgeAsRowGroupAndPatientIdAndGenderAsColumnGroupForObsReport() throws Exception {
        String reportName = "Aggregate Report with multiple Column Groups config";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("1000 1001", report.getRowAsString(2, " "));
        assertEquals("", report.getRowAsString(3, " "));
        assertEquals("3 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAggregateReportForVisitsReport() throws Exception {
        String reportName = "Aggregated report for visits report";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2017-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("Initial HIV Clinic Visit 1 1", report.getRowAsString(3, " "));
        assertEquals("Return TB Clinic Visit 1 0", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAggregateReportForProgramsReport() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs report";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("HIV PROGRAM 2", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowTotalColumnIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total column";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F Total", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("HIV PROGRAM 2 2", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowTotalRowIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total row";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("HIV PROGRAM 2", report.getRowAsString(3, " "));
        assertEquals("Total 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldShowTotalRowAndColumnIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total row and column";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F Total", report.getRowAsString(1, " "));
        assertEquals("", report.getRowAsString(2, " "));
        assertEquals("HIV PROGRAM 2 2", report.getRowAsString(3, " "));
        assertEquals("Total 2 2", report.getRowAsString(4, " "));
    }
}