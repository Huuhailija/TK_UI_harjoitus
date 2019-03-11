/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tkht2017;

import java.sql.Date;

/**
 *
 * @author Teemu
 */
public class excercise {

    // Excercise attributes
    int excercise_id;
    private String description;
    String example_answer;
    Date creation_date;
    int list_id;
    int teacher_id;
    
    // Constructor.
    public excercise(int excercise_id, String description, String example_answer, Date creation_date, int list_id, int teacher_id ) {
        this.excercise_id = excercise_id;
        this.description = description;
        this.example_answer = example_answer;
        this.creation_date = creation_date;
        this.list_id = list_id;
        this.teacher_id = teacher_id;   
    }
        
    public String getDescription(){
        return description;
    }
             
}
