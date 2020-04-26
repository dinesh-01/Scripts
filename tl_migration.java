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


public class ts {
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
     
		
      //Establish connection		
	   
	   String url = "http://xx.xxx.co/lib/api/xmlrpc/v1/xmlrpc.php";
       String devKey = "apikey";
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
		
		
		//Creating Test Suits
		int pid          = 1595;
		String prevSuite = null;
		HashMap<String, Integer> tsdetail = new HashMap<>(); 
		
        String tmquery      = "select distinct Section from test where Section like 'Transit delay as percentage-OEL%'";
		Statement tmstmt    = conn.createStatement();
		ResultSet tmresults = tmstmt.executeQuery(tmquery);
		
		while (tmresults.next()) {
			
			System.out.println(tmresults.getString("Section"));
			int parentid     = 7454;
			
			
			String[] split = tmresults.getString("Section").split(">");
			int len = split.length;
			int i = 0;
			String names = null;
			do {
				
				String suiteName = split[i].trim();
				
				if(i!=0) {
					
					prevSuite = split[i-1].trim();
					System.out.println(tsdetail +"=>"+i+"=>"+prevSuite);
					
					if(tsdetail.containsKey(prevSuite)) {
						parentid  = tsdetail.get(prevSuite);
						System.out.println(parentid + " included");
					}
						
					
					names += " > " + suiteName ;
					
				}else {
					names = suiteName;
				}
				
				//System.out.println(names);
				
								
				try {
					
					TestSuite ts = api.createTestSuite(pid,suiteName,suiteName,parentid,1,true,ActionOnDuplicate.BLOCK); 
					tsdetail.put(suiteName, ts.getId());
					
					//System.out.println("************");
					//System.out.println(suiteName);
					//System.out.println(ts.getId());
					//System.out.println("************");
					
					String query = "select * from test where Section = '"+names+"'";
					
					Statement stmt = conn.createStatement();
					ResultSet results = stmt.executeQuery(query);
					
					while (results.next()) {
						
						List<TestCaseStep> steps = new ArrayList<TestCaseStep>();
					    ExecutionType type       = null;
					       
				        TestCaseStep step = new TestCaseStep();
				        step.setNumber(1);
				        step.setExpectedResults(results.getString("Expected Results"));
						       
				        if(results.getString("Automated").equalsIgnoreCase("Yes")) {
				    	   step.setExecutionType(ExecutionType.AUTOMATED);
				    	    type = ExecutionType.AUTOMATED;
				    	   
				        }else {
				    	   step.setExecutionType(ExecutionType.MANUAL);
				    	    type = ExecutionType.MANUAL;
				        }
				        
				        step.setActions(results.getString("Steps to Execute"));
					    steps.add(step);
					           
					           TestCase tc = api.createTestCase(
					        		   
					        		results.getString("Title"), // testCaseName
					        		ts.getId(), // testSuiteId
					        		pid, // testProjectId
					                "dinesh", // authorLogin
					                "No summary", // summary
					                steps, // steps
					                results.getString("Pre-requisite"), // preconditions
					                TestImportance.HIGH, // importance
					                type, // execution
					                1, // order
					                1, // internalId
					                null, // checkDuplicatedName 
					                null
					               
					               
					           ); 
						
									
						       System.out.println(results.getString("Title"));

					
				    }
					
					
					
				} catch (TestLinkAPIException e) {
					System.out.println(suiteName +" already exists");
					
				}
				
				i++;
				
			}while(i<len);
			
			
	       
	     }
	}
}	
		
		
	
