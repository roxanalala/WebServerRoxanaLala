package src;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class WebServer extends Thread{
    private Socket connect;
    ConfigManager configManager;

    public WebServer(Socket c, ConfigManager configManager) {
        connect = c;
        this.configManager = configManager;
    }

    @Override
    public void run() {
        // we manage our particular client connection
        BufferedReader in = null; 
        PrintWriter out = null; 
        BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken().toLowerCase();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                if (Configuration.verbose) {
                    System.out.println("501 Not Implemented : " + method + " method.");
                }

                // we return the not supported file to the client
                File file = new File(configManager.getWebRootFile(), configManager.getNotSuportedPage());
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                //read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            }
            
            else {
            	
                // GET or HEAD method
            	
            	if(configManager.getState().equals("running")) {
            		if (fileRequested.endsWith("/")) {
                        fileRequested += configManager.getDefaultPage();
                    }

                    File file = new File(configManager.getWebRootFile(), fileRequested);
                    int fileLength = (int) file.length();
                    String content = getContentType(fileRequested);

                    if (method.equals("GET")) { // GET method so we return content
                        byte[] fileData = readFileData(file, fileLength);

                        // send HTTP Headers
                        out.println("HTTP/1.1 200 OK");
                        out.println("Server: Java HTTP Server");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + content);
                        out.println("Content-length: " + fileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush(); // flush character output stream buffer

                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();
                    }
                    
            	}
            	
            	else if(configManager.getState().equals("stopped")) {

                    File file = new File(configManager.getWebRootFile(), "not_supported.html");
                    int fileLength = (int) file.length();
                    String content = getContentType("not_supported.html");

                    if (method.equals("GET")) { // GET method so we return content
                        byte[] fileData = readFileData(file, fileLength);

                        // send HTTP Headers
                        out.println("HTTP/1.1 200 OK");
                        out.println("Server: Java HTTP Server");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + content);
                        out.println("Content-length: " + fileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush(); // flush character output stream buffer

                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();
                    }
            		
            	}
            	
            		else {
            			File file = new File(configManager.getWebRootFile(), "maintenance.html");
                        int fileLength = (int) file.length();
                        String content = getContentType("maintenance.html");

                        if (method.equals("GET")) { // GET method so we return content
                            byte[] fileData = readFileData(file, fileLength);

                            // send HTTP Headers
                            out.println("HTTP/1.1 200 OK");
                            out.println("Server: Java HTTP Server");
                            out.println("Date: " + new Date());
                            out.println("Content-type: " + content);
                            out.println("Content-length: " + fileLength);
                            out.println(); // blank line between headers and content, very important !
                            out.flush(); // flush character output stream buffer

                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();
            		
            		}
        

                if (Configuration.verbose) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }

            }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (Configuration.verbose) {
                System.out.println("Connection closed.\n");
            }
        }


    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    String fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(configManager.getWebRootFile(), configManager.getNotFoundPage());
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (Configuration.verbose) {
            System.out.println("File " + fileRequested + " not found");
            return ("File " + fileRequested + " not found");
        }
        return ("file found");
    }
}
