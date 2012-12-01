
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import sun.misc.BASE64Encoder;

public class StreamingConnection {
    public static void main(String... args) throws IOException {

	String username = ""; //gnip username
        String password = ""; //gnip password
	String streamURL = ""; // gnip connecting stream url

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        PrintWriter writer=null;

        try {
            connection = getConnection(streamURL, username, password);

            inputStream = connection.getInputStream();
            
            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode <= 299) {

               	BufferedReader reader = new BufferedReader(new InputStreamReader(new StreamingGZIPInputStream(inputStream)));
		String line = reader.readLine();
		 writer = new PrintWriter("gnipdata.txt");
		
		 long start = System.currentTimeMillis();
	
			while ( line!=null)    		
			{
	            		    
	                    System.out.println(line);
	                    writer.println(line);
	                    line = reader.readLine();
	                   }   
	                	
              
            } else {
                handleNonSuccessResponse(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                handleNonSuccessResponse(connection);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
                writer.close();
            }
        }
    }

    private static void handleNonSuccessResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        System.out.println("Non-success response: " + responseCode + " -- " + responseMessage);
    }

    private static HttpURLConnection getConnection(String urlString, String username, String password) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(1000 * 60 * 60);
        connection.setConnectTimeout(1000 * 10);

        connection.setRequestProperty("Authorization", createAuthHeader(username, password));
        connection.setRequestProperty("Accept-Encoding", "gzip");

   return connection;
    }

    private static String createAuthHeader(String username, String password) throws UnsupportedEncodingException {
        BASE64Encoder encoder = new BASE64Encoder();
        String authToken = username + ":" + password;
        return "Basic " + encoder.encode(authToken.getBytes());
    }
}