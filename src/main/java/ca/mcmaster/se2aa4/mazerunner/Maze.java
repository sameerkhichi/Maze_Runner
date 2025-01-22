/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze{

    private final Logger mazeLogger = LogManager.getLogger();

    //private instance variables to hold maze information
    private char[][] maze;
    private int rows;
    private int cols;
    private int[] entry;
    private int[] exit;

    public char[][] getMaze(){
        return maze;
    }
    
    public int[] getEntry(){
        return entry;
    }

    public int[] getExit(){
        return exit;
    }

    //Function to create a maze
    public void createMaze(String filepath){

        try{
            mazeLogger.info("** Starting Maze Runner");

            mazeLogger.info("**** Reading the maze from file " + filepath);
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine(); //reads the first line

            //incase the maze file that is passed is empty
            if(line == null){
                reader.close();
                throw new IllegalArgumentException("Maze does not exist");
            }
            
            //reading the first line gives amount of columns
            cols = line.length(); 
            rows = 1; //first row (zeroth index) is already read
            
            //figuring out how many rows there are
            while ((line = reader.readLine()) != null) {
                rows++;
            }

            //remaking the reader to start from beggining of file 
            reader.close(); 
            reader = new BufferedReader(new FileReader(filepath));

            maze = new char[rows][cols];

            //now store initialized maze array
            int row = 0;  
            while((line = reader.readLine()) != null){
                for(int col = 0; col < cols; col++){
                    maze[row][col] = line.charAt(col);
                }
                row++;
            }

            reader.close();
            mazeLogger.info("Maze has dimensions " + rows + " by " + cols);
        }
        catch(Exception e){
            mazeLogger.error("/!\\ An error has occured /!\\");
        }
    }
}
