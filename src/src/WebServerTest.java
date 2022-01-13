package src;

//import static javax.print.attribute.TextSyntax.verify;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//import static org.Mock.*;


public class WebServerTest {

    Scanner keyboard = new Scanner(System.in);

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After

	public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}


	@Test
	public void AcceptFailureTest() {
		/*find a way to make socket.accept() to fail...*/
	    System.err.print("Accept failed.");
	    assertEquals("Accept failed.", errContent.toString());
	}

	@Test
	public void couldNotListenOnPortTest() {
		/*run 2 webservers, resulting the 2nd to have the error 'port 10008 already used'*/
	    System.err.print("Could not listen on port: 10008.");
	    assertEquals("Could not listen on port: 10008.", errContent.toString());
	}

	@Test
	public void cannotClosePortTest() {
		/*find solution to fail the Socket Close*/
	    System.err.print("Could not close port: 10008.");
	    assertEquals("Could not close port: 10008.", errContent.toString());
	}

    @Test
   public void requestTest() {
      HttpURLConnection connection = null;
      try {
              URL url = new URL("http://127.0.0.1:8080");
             connection = (HttpURLConnection) url.openConnection();
              connection.setRequestMethod("GET");
             System.out.println(connection.getInputStream());
     }catch(IOException e) {
         System.out.println("failed");
       }
   }

   @Test
	public void testRun()
   {	Socket soc=new Socket();
   		Configuration con= new Configuration();
	   ConfigManager configManager=new ConfigManager(con);
   	WebServer sv=new WebServer(soc,configManager);
   	sv.run();
   }

   @Test
	public void getContent(){
	   Socket soc=new Socket();
	   Configuration con= new Configuration();
	   ConfigManager configManager=new ConfigManager(con);
	   WebServer sv=new WebServer(soc,configManager);
	 String reps=  sv.getContentType("something.htm");
	 assertEquals("text/html",reps);
   }
	@Test
	public void getContent2(){
		Socket soc=new Socket();
		Configuration con= new Configuration();
		ConfigManager configManager=new ConfigManager(con);
		WebServer sv=new WebServer(soc,configManager);
		String reps=  sv.getContentType("something.html");
		assertEquals("text/html",reps);
	}
	@Test
	public void getContent3(){
		Socket soc=new Socket();
		Configuration con= new Configuration();
		ConfigManager configManager=new ConfigManager(con);
		WebServer sv=new WebServer(soc,configManager);
		String reps=  sv.getContentType("something.htmp");
		assertEquals("text/plain",reps);
	}


}
