package controllers;

import play.*;
import play.mvc.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import models.*;

public class Application extends Controller {
 static String tablename=null;
 static boolean flag=false;

public static void process(String textbox)
{   
	tablename=textbox;
	flag=true;
	index();
}


public static void index() {
	
	
    	if(!flag)
    	{
    		tablename="china"; // default table name to be shown on the index page start
    	}
    	
    	
    	 String browser[]=new String[1000];
    	 float browsercount[]=new float[1000];
    	 int tweetcount[]=new int[1000];
    	 int retweetcount[]=new int[1000];
    	 String postedtime[]=new String[1000];
    	 int twittertabletotaltweets=0;
    	 int twittertabletotalretweets=0;
    	 int chinatabletotaltweets=0;
    	 int chinatabletotalretweets=0;
    	 String cityname[]=new String[1000];
    	 int citycount[]=new int[1000];
    	 int count=0;
    	 List<Object> citynamelist= new ArrayList<Object>();
	 List<Object> citycountlist=new ArrayList<Object>();
	     
	     
	     
    String others="others";
     float otherscount;
    
    	try{
    	
    		  Connection connect = null;
    	      Statement statement = null;
    	      ResultSet resultSet = null;
    	    
              try {
		    	        // This will load the MySQL driver, each DB has its own driver
		    	      	// System.out.println("locating error");
		    	        Class.forName("com.mysql.jdbc.Driver");
    	       
		    	        // Setup the connection with the DB
		    	        connect = DriverManager
		    	            .getConnection("jdbc:mysql://localhost/gnipdata?"
		    	                + "user=&password=");

		    	        // Statements allow to issue SQL queries to the database
		    	        statement = connect.createStatement();
		    	        // Result set get the result of the SQL query
		    	        resultSet = statement
		    	            .executeQuery("select browser, count(browser) from gnipdata."+tablename+" group by browser order by count(browser) desc limit 10;");  // limit accourding to the data volume that has to be rendered to the UI
		    	        
		    	        
    	                while (resultSet.next()) {
        	        
								        	      	browser[count]=resultSet.getString("browser");
								        	      	browsercount[count]=resultSet.getInt("count(browser)");
								        	      	count++;
        	                                        //System.out.println("browser: "+browser+"    count:"+count);
        	                                      }
    	        
			    	    resultSet=statement.executeQuery("select postedtime,count(postedtime),sum(retweetcount) from gnipdata."+tablename+" group by postedtime order by postedtime;");
			    	    
			    	    count=0;
			    	    while(resultSet.next()){      	
    	   
								    	        	postedtime[count]=resultSet.getString("postedtime");
								    	        	tweetcount[count]=resultSet.getInt("count(postedtime)");
								    	        	retweetcount[count]=resultSet.getInt("sum(retweetcount)");
								    	        	count++;
    	        	
    	                                        }
    	        
    	        resultSet=statement.executeQuery("select count(twitterid),sum(retweetcount) from gnipdata.twitter;");
                while(resultSet.next()){
					                	twittertabletotaltweets=resultSet.getInt("count(twitterid)");
					                	twittertabletotalretweets=resultSet.getInt("sum(retweetcount)");
                         	
                                       }
                
                resultSet=statement.executeQuery("select count(twitterid),sum(retweetcount) from gnipdata.china;");
                while(resultSet.next())
                {
                	chinatabletotaltweets=resultSet.getInt("count(twitterid)");
                	chinatabletotalretweets=resultSet.getInt("sum(retweetcount)");
                }
                
                //System.out.println("printing location results");
                
                resultSet=statement.executeQuery("select location,count(location) from "+tablename+" group by location order by count(location);");
                count=0;
                while(resultSet.next())
                {   
                	cityname[count]=resultSet.getString("location");
                	citycount[count]=resultSet.getInt("count(location)");
                	
                	citynamelist.add(cityname[count]);
	      	    	citycountlist.add(citycount[count]);
	      	    	System.out.println(citynamelist.get(count));
	      	    	System.out.println(citycountlist.get(count));
	      	    	count++; 
                }
    	        	
    	       
    	      
    	     
    	       
    	    
    	      } 
    	      
    	      catch (Exception e) {
    	    	  					System.out.println(e);
    	                          }
    	      
    	      finally {
				    	    	  //System.out.println("in the closing part");
				        	      try {
				        	        if (resultSet != null) 
				        	                  resultSet.close();
				        	        				
				        	        if (statement != null) 
				        	                 statement.close();
				        	        				
				        	        if (connect != null) 
				        	          connect.close();
				        	        
				        	           } 
				        	      catch (Exception e) {
				        	    	      	    	  System.out.println(e);
								          }
				    	   //System.out.println("closing jdbc");
    	              }
  	       		
    	       }
    	
    	catch(Exception e)
    	{
    		System.out.println("error in jdbc connection part");
    	}
    	
	for(int i=0;i<10;i++)
	{
	    	System.out.println("Browser:"+browser[i]+" count:"+browsercount[i]);  
	}
	
	List<Object> browserlist=new ArrayList<Object>();
	List<Object> browsercountlist=new ArrayList<Object>();
	List<Object> browserpercentagelist=new ArrayList<Object>();
	int sum=0;
	for(int i=0;i<10;i++)
	{
		browserlist.add(browser[i]);
		browsercountlist.add(browsercount[i]);
		int sum=sum+browsercountlist[i];
	}
	
	
	for(int i=0;i<10;i++)
	{
		browserpercentagelist.add((float)(browsercountlist[i]/sum)*100);
	}
	
		
	
	
	List<Object> postedtimelist=new ArrayList<Object>();
	for(int i=0;i<20;i++)
	{  
		
		try{
		
		postedtimelist.add(postedtime[i].substring(17,19));
		}
		catch(NullPointerException e)
		{
			System.out.println(e);
		}
		
	}
	
	
	List<Object> tweetcountlist=new ArrayList<Object>();
	List<Object> retweetcountlist=new ArrayList<Object>();
	for(int i=0;i<10;i++){
		tweetcountlist.add(tweetcount[i]);
		retweetcountlist.add(retweetcount[i]);
		System.out.println(tweetcountlist.get(i));
		
	}
	
	
	
          render(browserlist,
                 browserpercentagelist,
		 postedtimelist,
		 tweetcountlist,
		 retweetcountlist,
		 twittertabletotaltweets,twittertabletotalretweets,
		 chinatabletotaltweets,chinatabletotalretweets,
                 citynamelist,citycountlist);
    
}
    
}
    	
    