package tools;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.ResponseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.model.CustomField;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

/**
 * API interface to let fitnesse to interact with testlink
 * @author Administrator
 *
 */


public class update {
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
     
		
      //Establish connection		
	   
	   String url = "https://XXX/lib/api/xmlrpc/v1/xmlrpc.php";
       String devKey = "XXXX";
       TestLinkAPI api = null;
       
       URL testlinkURL = null;
       
       try     {
               testlinkURL = new URL(url);
       } catch ( MalformedURLException mue )   {
               mue.printStackTrace( System.err );
               System.exit(-1);
       }
       
       try     {
               api = new TestLinkAPI(testlinkURL, devKey);
       } catch( TestLinkAPIException te) {
               te.printStackTrace( System.err );
               System.exit(-1);
       }
       
       //System.out.println(api.ping());
       
       
        String filePath = "/Users/dinesh/Documents/csv";
	   	Class.forName("org.relique.jdbc.csv.CsvDriver");
		String csvurl = "jdbc:relique:csv:" + filePath + "?" + "separator=,"+ "&" + "fileExtension=.csv";
		Properties props = new Properties();
		props.put("suppressHeaders", "false");
		Connection conn = DriverManager.getConnection(csvurl, props);
		
		
			
        String tmquery      = "select id from testcase";
		Statement tmstmt    = conn.createStatement();
		ResultSet tmresults = tmstmt.executeQuery(tmquery);
		
		while (tmresults.next()) {
			
			String ids = tmresults.getString("id");
			String id[] = ids.split(":");
			System.out.println(id[0]);
			
			TestCase testcase = api.getTestCaseByExternalId(id[0], 1);
		    api.setTestCaseExecutionType(1595, testcase.getId(), null, 1, ExecutionType.AUTOMATED);
			
			
				
				
	    }
		
			
			
	       
	     }
	}	
		
		
	
