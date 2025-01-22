/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

public class Main {

    //Private object instances for Main class/information hiding
    private static final Logger logger = LogManager.getLogger();
    private static Options options = new Options();
    
    //private variable for the filepath
    private String filepath;

    public static void main(String[] args) {

        //sorry I know these lines are long, its for a concise description
        options.addOption("i", false, "Maze File - Specifies the filename to be used");
        options.addOption("p", false, "Path sequence - Activates path verification mode to check if the entered sequence solves the maze");

        try{
            for(int i=0; i < args.length; i++){
                
                logger.debug("Checks flags", args[i]);
                if(options.hasOption("-i")){
                    //creating an instance of the Main class object
                    Main main = new Main(args[i+1]);
                    //starting the game 
                    main.startGame();
                    break;
                }
            }
        }

        catch(Exception e){
            logger.error("/!\\ An error has occured /!\\");
        }

        logger.info("**** Computing path");
        logger.info("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }

    //constructor for the main class
    public Main(String filepath){
        this.filepath = filepath;
        logger.debug("The filepath given", filepath);
    }

    //getter for the filepath/encapsulating it
    public String getFilePath(){
        return filepath;
    }

    //method to start the game
    public void startGame(){

        //creates a new instance of the maze object and calls a function to create the maze
        Maze maze = new Maze();
        maze.createMaze(getFilePath());
    }
}