/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tkht2017;
import java.util.List;
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Teemu
 */
public class backEnd {
    
    private static final String AJURI = "org.postgresql.Driver";
    private static final String PROTOKOLLA = "jdbc:postgresql:";
    private static final String PALVELIN= "localhost";
    private static final int PORTTI = 5432;
    private static final String TIETOKANTA = "TIKO17";  // tähän oma käyttäjätunnus
    private static final String KAYTTAJA = "postgres";  // tähän oma käyttäjätunnus
    private static final String SALASANA = "tietokanta";  // tähän tietokannan salasana
 
    private Connection con = null;
    private boolean returnParameter;
    private String error = "";
    private List<excercise> excerciseList = new ArrayList<excercise>();
    private int sessionNumber;
    private int currentExcercise = 0;
    private int tryNumber = 1;
    
    

    
    
    public boolean startCon() {
        
        // Ladataan ajuri
        try {
            Class.forName(AJURI);
        } catch (ClassNotFoundException e) {
            error = e.getMessage();
            returnParameter = false;
            return returnParameter;
        }
        
        // Muodostetaan yhteys  
        try {
            con = DriverManager.getConnection(PROTOKOLLA + "//" + PALVELIN + ":" + PORTTI + "/" + TIETOKANTA, KAYTTAJA, SALASANA);
            returnParameter = true;
        } catch (Exception e) {
            error = e.getMessage();
            returnParameter = false;
            return returnParameter;
        } 
   
        return returnParameter;
    }
    
    public boolean endCon() {        
        // Lopetetaan yhteys
        if (con!= null) try {
            con.close();
            returnParameter = true;
        } catch (SQLException e) {
            error = e.getMessage();
            returnParameter = false;
            return returnParameter;
        }
        
        return returnParameter;
    }    

    public String getError(){
        return error;
    }
    
    public void initialize() {
        //Alustetaan tietokantaan uusi sessio sekä yritys.
        getExcercises();
        startSession();
        startTry();
    }
    
    public int getDBIdCount(String table) {
    // ID- laskuritietokantaan.
    int countResult = 0;      
        try {    
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT " + table + "_id " + " FROM public." + table + "\n" + ";");

            while (rset.next()) {
                countResult++;             
            }
           
        }catch (SQLException e) {

        }
        countResult++;
        return countResult;
    }
    
    public void startTry(){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO public.\"Tries\"(\n" +
"	session_id, excercise_id, try_num, start_time, end_time, answer)\n" +
"	VALUES (" + sessionNumber + "," + excerciseList.get(currentExcercise).excercise_id + "," + tryNumber +", current_time, null, null);");
        }
        catch (SQLException e) {
            error = e.getMessage();
        }
    }
    
    public void endTry(String answer){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE public.\"Tries\" SET end_time = current_time, answer = '" + answer + "'\n" +
                                "WHERE try_num = " + tryNumber + ";");         
        }
        catch (SQLException e) {
            error = e.getMessage();
        }
    }
    
    public List<excercise> getExcerciseList(){
        return excerciseList;
    }
    
    public int currentExcercise(){         
        return currentExcercise;
    }
    
    public void nextExcercise(){
        currentExcercise++;
    }
        
    public void nextTry(){
        tryNumber++;
    }
    
    public void resetTry(){
        tryNumber = 1;
    }
    
    public int currentTry(){
        return tryNumber;
    }
    
    
    public void startSession(){
        // Get ID for this session.
        sessionNumber = getDBIdCount("session");
        
        try{
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO public.session(\n" +
"	session_id, start_time, end_time, student_id)\n" +
"	VALUES ("+ sessionNumber +", current_time, null, 424083);");
        }
        catch (SQLException e) {
            error = e.getMessage();
        }
    }
    
    public void endSession(){
        if (con != null){           
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("UPDATE session SET end_time = current_time\n" +
                                "WHERE session_id = " + sessionNumber + ";");
            }
            catch (SQLException e) {
                error = e.getMessage();
            }
        }
    }
    public void getExcercises() {        
        try {    
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM public.excercise;");

            while (rset.next()) {
                int excercise_id = rset.getInt("excercise_id");
                String description = rset.getString("description");
                String example_answer = rset.getString("example_answer");
                Date creation_date = rset.getDate("creation_date");
                int list_id = rset.getInt("list_id");
                int teacher_id = rset.getInt("teacher_id");
                                               
                excerciseList.add(new excercise(excercise_id,description,example_answer,creation_date,list_id,teacher_id));
            }                     
        }catch (SQLException e) {
            error = e.getMessage();
        }
    }
}
