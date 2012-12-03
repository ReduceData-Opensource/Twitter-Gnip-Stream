
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;


public class Parser {
    
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        Gnipdatainsert1 obj=new Gnipdatainsert1();
        PrintWriter writer=null;
        String postedtime,twitterid,location,keyword,browser;
        int retweetcount;
        
        JsonStreamParser parser = new JsonStreamParser(new FileReader("gnipdata.txt"));
        try{
            Thread.currentThread().sleep(5000)//  Wait for the Streaming connection to write a complete data for a single user
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        boolean flag=false;
        
        while(!flag){
            try{
                
                if(parser.hasNext()){
                    
                    writer = new PrintWriter("support1.txt");
                    System.out.println("Reading a user gson data from the large gnip stream");
                    writer.println(parser.next());
                    writer.close();
                    try{
                        
                        
                        
                        try{
                            
                            JsonReader reader0 = new JsonReader(new FileReader("support1.txt"));
                            JsonParser jsonparser0=new JsonParser();
                            JsonElement ele=jsonparser0.parse(reader0)
                            .getAsJsonObject().get("postedTime");
                            postedtime=ele.getAsString();
                        }
                        catch(NullPointerException n)
                        {
                            postedtime="nill";
                        }
                        
                        
                        // For getting retweet count
                        try{
                            JsonReader reader1 = new JsonReader(new FileReader("support1.txt"));
                            JsonParser jsonparser1=new JsonParser();
                            JsonElement ele1=jsonparser1.parse(reader1)
                            .getAsJsonObject().get("retweetCount");
                            retweetcount=ele1.getAsInt();
                            // System.out.println(retweetcount);
                        }
                        catch(NullPointerException n)
                        {
                            retweetcount=0;
                        }
                        
                        
                        // For getting browser display name
                        try{
                            JsonReader reader2 = new JsonReader(new FileReader("support1.txt"));
                            JsonParser jsonparser2=new JsonParser();
                            JsonElement ele2=jsonparser2.parse(reader2)
                            .getAsJsonObject().get("generator")
                            .getAsJsonObject().get("displayName");
                            browser=ele2.getAsString();
                            //twitterid=ele2.getAsString();
                            //System.out.println(browser);
                        }
                        catch(NullPointerException n)
                        {
                            browser="null";
                        }
                        
                        // For getting keywords
                        
                        try{
                            JsonReader reader3 = new JsonReader(new FileReader("C:/Users/SriBalaKumar/Documents/support1.txt"));
                            JsonParser jsonparser3=new JsonParser();
                            JsonElement ele3=jsonparser3.parse(reader3)
                            .getAsJsonObject().get("gnip")
                            .getAsJsonObject().getAsJsonArray("matching_rules").get(0)
                            .getAsJsonObject().get("value");
                            keyword=ele3.getAsString();
                            // System.out.println(keyword);
                        }
                        catch(NullPointerException n)
                        {
                            keyword="null";
                        }
                        
                        
                        
                        // Get timezone rather then actor---&gt;location---&gt;displayName as the same data is not always present.
                        try{
                            JsonReader reader4 = new JsonReader(new FileReader("support1.txt"));
                            JsonParser jsonparser4=new JsonParser();
                            JsonElement ele4=jsonparser4.parse(reader4)
                            .getAsJsonObject().get("actor")
                            .getAsJsonObject().get("twitterTimeZone");
                            
                            location=ele4.getAsString();
                            //System.out.println(location);
                        }
                        catch(Exception e)
                        {   location="null";
                            System.out.println("TimeZone not found");
                        }
                        
                        // For getting twitter-id , actor-&gt;id
                        
                        try{
                            JsonReader reader5 = new JsonReader(new FileReader("support1.txt"));
                            JsonParser jsonparser5=new JsonParser();
                            JsonElement ele5=jsonparser5.parse(reader5)
                            .getAsJsonObject().get("actor")
                            .getAsJsonObject().get("id");
                            twitterid=ele5.getAsString();
                        System.out.println(twitterid); }
                        catch(NullPointerException n)
                        {
                            twitterid="null";
                        }
                        
                        
                        // For getting the json data for single user
                        
                        InputStream is = new FileInputStream("support1.txt");
                        StringBuffer buffer = new StringBuffer();
                        byte[] b = new byte[4096];
                        for (int n; (n = is.read(b)) != -1;) {
                            buffer.append(new String(b, 0, n));
                        }
                        String str = buffer.toString();
                        System.out.println(str);
                        //System.out.println("end of 1 user");
                        
                        
                        
                        //Writing to database
                        
                        try {
                            Thread.currentThread().sleep(2000);
                            obj.readDataBase(twitterid,postedtime,location,retweetcount,keyword,browser,str);
                            
                        }
                        catch (Exception e) {
                            
                            Thread.currentThread().sleep(5000); //  To be in synchoronous with the database write-record
                            
                            // call the write to the database
                            obj.readDataBase(twitterid,postedtime,location,retweetcount,keyword,browser,str);
                        }
                        
                        
                        
                    }
                    catch (FileNotFoundException e) {System.out.println(e);} catch (IOException e) {System.out.println(e);}
                    
                }
                
                else
                {
                    Thread.currentThread().sleep(5000); // wait to accomadate the gnip record delay
                }
                
                
                
                
                
            }
            catch(Exception e)
            {
                System.out.println(e);
                Thread.currentThread().sleep(3000); // set the timer according to the keep alive timer of the gnip stream
                
            }
            
        }
        
    }
    
}




class Gnipdatainsert1 {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    
    public void readDataBase(String t_id, String p_time, String t_location, int r_count, String t_keyword,String t_browser,String gnip_json) throws Exception {
        try {
            // This will load the MySQL driver, each DB has its own driver
            // System.out.println("locating error");
            Class.forName("com.mysql.jdbc.Driver");
            
            // Setup the connection with the DB
            connect = DriverManager
            .getConnection("jdbc:mysql://localhost/gnipdata?"
            + "user=&password="); // Corresponding User name and password to the database
            
            
            statement = connect.createStatement();
            preparedStatement = connect
            .prepareStatement("insert into  gnipdata.twitter values (?, ?, ?, ?, ? ,?,?)");
            
            preparedStatement.setString(1, t_id);
            preparedStatement.setString(2, p_time);
            preparedStatement.setString(3, t_location);
            preparedStatement.setInt(4,r_count);
            preparedStatement.setString(5, t_keyword);
            preparedStatement.setString(6, t_browser);
            preparedStatement.setString(7, gnip_json);
            
            
            preparedStatement.executeUpdate();
            //System.out.println("After Inserting into table the values are");
            preparedStatement = connect
            .prepareStatement("SELECT twitterid, postedtime, location, retweetcount, keyword, browser, userjson from gnipdata.twitter");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);
            
            
            } catch (Exception e) {
            System.out.println(e);
            } finally {
            close();
            // System.out.println("closing jdbc");
            // System.out.println("------------------------------------------------------------------");
            
        }
        
    }
    
    
    private void writeResultSet(ResultSet resultSet) throws SQLException {
        
        while (resultSet.next()) {
            
            String twitterid = resultSet.getString("twitterid");
            String postedtime = resultSet.getString("postedtime");
            String twittertimezone = resultSet.getString("location");
            
            int retweetcount = resultSet.getInt("retweetcount");
            String browser=resultSet.getString("browser");
            String userjson=resultSet.getString("userjson");
            System.out.println("twitterid: " + twitterid);
            System.out.println("postedtime: " + postedtime);
            System.out.println("twittertimezone: " + twittertimezone);
            System.out.println("retweetcount: " + retweetcount);
            System.out.println("browser: " + browser);
            System.out.println("Userjson:"+userjson);
        }
    }
    
    //  need to close the resultSet
    private void close() {
        //System.out.println("in the closing part");
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
            
            if (connect != null) {
                connect.close();
            }
            } catch (Exception e) {
            System.out.println(e);
            
        }
    }
    
    
    
    
    
}