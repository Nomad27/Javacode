package com.mobiquity.assignment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


/**
 * Base test class.
 */
public class BaseTest {

	private static final Logger logger = LogManager.getLogger(BaseTest.class.getName());

	public static final String BASE_URI = "https://jsonplaceholder.typicode.com/";
	public static final String BASE_PATH_USERS = "users";
	public static final String BASE_PATH_POSTS = "posts";
	public static final String BASE_PATH_COMMENTS = "comments";
	public static ExtentReports extent;
	public static ExtentHtmlReporter reporter;
	public static ExtentTest extentlogger;
	public static ExtentTest childTest;
	
	static Date date = new Date();  
    static Timestamp ts=new Timestamp(date.getTime());  
    static SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");  
	private static String reportFileName = "mobiquity"+timestamp.format(ts)+".html";
    private static String reportFilepath = System.getProperty("user.dir")+"/src/main/java/Reports/";
    final static String filepath =  reportFilepath + reportFileName;
    

/** Method for initiate the Extent Report */
    @BeforeClass
    public void intiateReporting() {
    	
    	reporter = new ExtentHtmlReporter(filepath);
    	extent = new ExtentReports();
    	extent.setSystemInfo("Name", "Mobiquity Test Assignment");
    	extent.setSystemInfo("User Name", "Ritu Puneet Kumar");
    	reporter.config().setCSS("css-string");
    	reporter.config().setEncoding("utf-8");
    	reporter.config().setJS("js-string");
    	reporter.config().setDocumentTitle("Mobiquity Test Assignment");
    	reporter.config().setTheme(Theme.DARK);
    	extent.attachReporter(reporter);
    	extent.setReportUsesManualConfiguration(true);
    }
    
    @AfterMethod
    public void close()
    {
      
    	extent.flush();
    }
}
