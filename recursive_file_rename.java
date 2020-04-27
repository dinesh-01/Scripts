package tools;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

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


public class tr {
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
     
        //Tl link  
        String tlfilePath = "/Users/dinesh/Documents/csv/tl";
	   	Class.forName("org.relique.jdbc.csv.CsvDriver");
		String tlcsvurl = "jdbc:relique:csv:" + tlfilePath + "?" + "separator=,"+ "&" + "fileExtension=.csv";
		Properties tlprops = new Properties();
		tlprops.put("suppressHeaders", "false");
		Connection tlconn = DriverManager.getConnection(tlcsvurl, tlprops);
		
		//FR Link
		 String frfilePath = "/Users/dinesh/Documents/csv/fr";
	   	 Class.forName("org.relique.jdbc.csv.CsvDriver");
		 String frcsvurl = "jdbc:relique:csv:" + frfilePath + "?" + "separator=,"+ "&" + "fileExtension=.csv";
		 Properties frprops = new Properties();
		 tlprops.put("suppressHeaders", "false");
		 Connection frconn = DriverManager.getConnection(frcsvurl, frprops);
		
		
        String tlquery      = "select testcase from testlink";
		Statement tlsmt    = tlconn.createStatement();
		ResultSet tlresults = tlsmt.executeQuery(tlquery);
		
		while (tlresults.next()) {
			

			String[] split = tlresults.getString("testcase").split(":");
			String tl_id   = split[0];
			String tl_name = split[1];
			
			System.out.println(tl_id);
			System.out.println(tl_name);
			
			String    frquery   = "select ID from frelease where Title = '"+tl_name+"'";
			Statement frsmt     = frconn.createStatement();
			ResultSet frresults = frsmt.executeQuery(frquery);
			
			int i=1;			
			while (frresults.next()) {
				
				if(i==1) {
					System.out.println(frresults.getString("ID"));
					
					
					 File root = new File("/Users/dinesh/Documents/GitHub/automation/workflow/FitNesseRoot/FrontPage/pando");
				        String fileName = frresults.getString("ID")+".wiki";
				        try {
				            boolean recursive = true;

				            Collection files = FileUtils.listFiles(root, null, recursive);
				            String frname = null;
				            String tlink  = null;

				            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				            	
				                File file = (File) iterator.next();
				                
				                if (file.getName().equals(fileName)) {
				                	
				                	 frname = file.getAbsolutePath();
					                 System.out.println(frname);
					                 tlink  = frname.replace(fileName, tl_id+".wiki");
					                 System.out.println(tlink);
					                 
					                 File oldfile =new File(frname);
					         		 File newfile =new File(tlink);
					                 
					                 if(oldfile.renameTo(newfile)){
					         			System.out.println("Rename succesful");
					         		}else{
					         			System.out.println("Rename failed");
					         		}
				                
				                
				                }
				                    
				                	
				                   
			                     //String tlink  = frname.replace(fileName, tl_id+".wiki");
			                     
			                     //System.out.println(tlink);
				            }
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				}
				
				
				i++;
				
			}
			
			System.out.println("*******************");
			
	    }
    }
	
}	
	
		
		
	
