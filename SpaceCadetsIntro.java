/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
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
}
