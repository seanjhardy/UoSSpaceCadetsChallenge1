/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacecadetsintro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seanjhardy
 */
public class SpaceCadetsIntro {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        SpaceCadetsIntro programInstance = new SpaceCadetsIntro();
    }
    
    public SpaceCadetsIntro(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            //print help message
            System.out.println("COMMANDS: \n" +
                    "UniversityID - search a user \n"+
                    "Q - quit the program");
            System.out.print("> ");
            String command = scanner.next();
            
            if(command.equals("Q")){
              //Print thank you message and exit the loop
              System.out.println("Thank you for using this system");
              break;
            }
            
            try {
                URL url = new URL("https://www.ecs.soton.ac.uk/people/" + command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                String source = "";
                while ((inputLine = reader.readLine()) != null){
                    source += inputLine;
                }
                String title = fetchTitle(source);
                if(("Invalid User").equals(title)){
                  System.out.println("Invalid User");
                }else{
                  System.out.println("Title: " + title);
                  System.out.println("Email: " + command + "@soton.ac.uk");
                }
                reader.close();
            } catch (MalformedURLException ex) {
                Logger.getLogger(SpaceCadetsIntro.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SpaceCadetsIntro.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    public final String fetchTitle(String source) throws IOException{
        int startIndex = source.indexOf("property=\"name\"");
        if(startIndex == -1){
          return "Invalid User";
          //throw new IOException("Invalid user entered, please try again");
        }
        startIndex += 16;
        int endIndex = source.indexOf("<", startIndex)-1;
        return source.substring(startIndex, endIndex);
    }
    
    public final ArrayList<String> fetchEmails(String source){
        ArrayList<String> emails = new ArrayList<>();
        
        String[] invalidChars = {"\"", "'", ":", "<", ">"};
        //Get the number of emails by the number of instances of the @ symbol
        int numEmails = (source.split("@", -1).length) - 1;
        
        //for each email found
        for(int i = 0; i < numEmails; i++){
            //Get the ith email in the source code
            int emailIndex = nthOccurrence(source, "@", i);
              if(emailIndex > 0 && emailIndex < source.length()){
              //reverse the string to find the last instance of the > character
              String reverseString = new StringBuilder(source.substring(0, emailIndex)).reverse().toString();
              int startIndex = emailIndex - reverseString.indexOf(">");
              
              //find the next instance of the < character to end the substring
              int endIndex = source.indexOf("<", emailIndex);
              
              String email = source.substring(startIndex, endIndex);
              boolean validEmail = true;
              for(String s : invalidChars){
                if(email.contains(s)){
                  validEmail = false;
                }
              }
              if(validEmail){
                emails.add(email);
              }
            }
        }
        return emails;
    }
    
    public static int nthOccurrence(String str1, String str2, int n) {
        String tempStr = str1;
        int tempIndex;
        int finalIndex = 0;
        for(int occurrence = 0; occurrence < n ; occurrence += 1){
            tempIndex = tempStr.indexOf(str2);
            if(tempIndex == -1){
                finalIndex = 0;
                break;
            }
            tempIndex ++;
            tempStr = tempStr.substring(tempIndex);
            finalIndex += tempIndex;
        }
        
        return --finalIndex;
    } 
}
