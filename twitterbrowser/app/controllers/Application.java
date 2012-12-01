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
{   //System.out.println("Got from template--data--"+textbox);
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
		    	                + "user=sribalakumar1&password=sbk@laptop");

		    	        // Statements allow to issue SQL queries to the database
		    	        statement = connect.createStatement();
		    	        // Result set get the result of the SQL query
		    	        resultSet = statement
		    	            .executeQuery("select browser, count(browser) from gnipdata."+tablename+" group by browser order by count(browser) desc limit 10;");  // limit accourding to the data volume that has to be rendered to the UI
		    	        
		    	        int totalrowinquery=0;
    	                while (resultSet.next()) {
        	        
								        	      	browser[totalrowinquery]=resultSet.getString("browser");
								        	      	browsercount[totalrowinquery]=resultSet.getInt("count(browser)");
								        	      	totalrowinquery++;
        	                                        //System.out.println("browser: "+browser+"    count:"+count);
        	                                      }
    	        
			    	    resultSet=statement.executeQuery("select postedtime,count(postedtime),sum(retweetcount) from gnipdata."+tablename+" group by postedtime order by postedtime;");
			    	    
			    	    int trquery2=0;
			    	    while(resultSet.next()){      	
    	   
								    	        	postedtime[trquery2]=resultSet.getString("postedtime");
								    	        	tweetcount[trquery2]=resultSet.getInt("count(postedtime)");
								    	        	retweetcount[trquery2]=resultSet.getInt("sum(retweetcount)");
								    	        	trquery2++;
    	        	
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
                int ii=0;
                while(resultSet.next())
                {   
                	cityname[ii]=resultSet.getString("location");
                	citycount[ii]=resultSet.getInt("count(location)");
                	
                	citynamelist.add(cityname[ii]);
	      	    	citycountlist.add(citycount[ii]);
	      	    	System.out.println(citynamelist.get(ii));
	      	    	System.out.println(citycountlist.get(ii));
	      	    	ii++; 
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
    	
    		
    	   
	String browser1,browser2,browser3,browser4,browser5,browser6,browser7,browser8,browser9,browser10;
	float browsercount1,browsercount2,browsercount3,browsercount4,browsercount5,browsercount6,browsercount7,browsercount8,browsercount9,browsercount10;
	for(int i=0;i<10;i++)
	{
	    	System.out.println("Browser:"+browser[i]+" count:"+browsercount[i]);  
	}
	browser1=browser[0];
	browser2=browser[1];
	browser3=browser[2];
	browser4=browser[3];
	browser5=browser[4];
	browser6=browser[5];
	browser7=browser[6];
	browser8=browser[7];
	browser9=browser[8];
	browser10=browser[9];
	
	browsercount1=(browsercount[0]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount2=(float)(browsercount[1]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount3=(float)(browsercount[2]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount4=(float)(browsercount[3]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount5=(float)(browsercount[4]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount6=(float)(browsercount[5]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount7=(float)(browsercount[6]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount8=(float)(browsercount[7]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount9=(float)(browsercount[8]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	browsercount10=(float)(browsercount[9]/(browsercount[0]+browsercount[1]+browsercount[2]+browsercount[3]+browsercount[4]+browsercount[5]+browsercount[6]+browsercount[7]+browsercount[8]+browsercount[9]))*100;
	
	otherscount=100-(browsercount1+browsercount2+browsercount3+browsercount4+browsercount5+browsercount6+browsercount7+browsercount8+browsercount9+browsercount10);
	
	
	List<Object> postedtimelist=new ArrayList<Object>();
	for(int i=0;i<20;i++)
	{  
		//System.out.println("posted time:"+postedtime[i]+"retweetcount:"+retweetcount[i]+"tweetcount:"+tweetcount[i]);
		try{
		
		postedtimelist.add(postedtime[i].substring(17,19));
		}
		catch(NullPointerException e)
		{
			continue;
		}
		
	}
	
	
	
	
	System.out.println("checking arraylist");
	
	List<Object> tweetcountlist=new ArrayList<Object>();
	List<Object> retweetcountlist=new ArrayList<Object>();
	for(int i=0;i<10;i++){
		tweetcountlist.add(tweetcount[i]);
		retweetcountlist.add(retweetcount[i]);
		System.out.println(tweetcountlist.get(i));
		
	}
	
	
	
  render(browser1,browsercount1,browser2,browsercount2,browser3,browsercount3,browser4,browsercount4,browser5,browsercount5,browser6,browsercount6,browser7,browsercount7,browser8,browsercount8,browser9,browsercount9,browser10,browsercount10,others,otherscount,
		 postedtimelist,
		 tweetcountlist,
		 retweetcountlist,
		 twittertabletotaltweets,twittertabletotalretweets,
		 chinatabletotaltweets,chinatabletotalretweets,
         citynamelist,citycountlist);
    
}
    
}
    	
    