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

    //getters for all attributes of the maze object
    public char[][] getMaze(){
        return maze;
    }
    
    public int[] getEntry(){
        return entry; //stored as [entry row, entry column]
    }

    public int[] getExit(){
        return exit; //stored as [exit row, exit column]
    }

    //Function to create a maze
    public void createMaze(String filepath){

        try{
            mazeLogger.info("Starting Maze Runner");

            mazeLogger.info("Reading the maze from file " + filepath);
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine(); //reads the first line

            //incase the maze file that is passed is empty
            if(line == null){
                reader.close();
                mazeLogger.debug("Maze file is Empty");
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

                //this is called padding, makes it easier to deal with a special case of a straight maze
                //this also padds uneven line lengths to make them easier to deal with
                line = line + " ".repeat(Math.max(0, cols - line.length()));

                for(int col = 0; col < cols; col++){
                    maze[row][col] = line.charAt(col);

                    //this will check for and store the entry and exit points in a 1D array
                    if(col==0 && (line.charAt(col) == ' ')){
                        entry = new int[]{(row), 1}; //initial code setup used indexing that started at 1
                        mazeLogger.debug("Entry Stored");
                    }
                    if( (col == (cols - 1)) && (line.charAt(col) == ' ') ){
                        exit = new int[]{(row), cols};
                        mazeLogger.debug("Exit Stored");
                    }
                }
                row++;
            }
            mazeLogger.debug("Stored maze successfully");

            reader.close();
            mazeLogger.info("Maze has dimensions " + rows + " by " + cols);
            mazeLogger.info("Maze entry: " + entry[0] + "," + entry[1]);
            mazeLogger.info("Maze exit: " + exit[0] + "," + exit[1]);
        }
        catch(Exception e){
            mazeLogger.debug(e);
            mazeLogger.error("/!\\ An error has occurred /!\\");
        }
    }
}