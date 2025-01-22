/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

public class Main {

    //Private object instances for Main class/information hiding
    private static final Logger logger = LogManager.getLogger();
    private static Options options = new Options();
    
    //private variable for the filepath and checker for validation mode
    private String filepath;
    private boolean pathVerification;
    private String path;


    public static void main(String[] args) {

        //sorry I know these lines are long, its for a concise description
        options.addOption("i", false, "Maze File - Specifies the filename to be used");
        options.addOption("p", false, "Path sequence - Activates path verification mode to check if the entered sequence solves the maze");

        try{
            for(int i=0; i < args.length; i++){
                
                logger.debug("Checks flags", args[i]);
                
                //allowing for maze file insertion
                if(options.hasOption("-i")){
                    //creating an instance of the Main class 
                    Main main = new Main(args[i+1],false, args[i+3]);
                    //starting the game 
                    main.startGame();
                    break;
                }
                //allowing for path verification
                if(options.hasOption("-p")){
                    //creating a instance of main class with verification mode active
                    Main main = new Main(args[i+1],true,null);
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
    public Main(String filepath, boolean pathVerification, String path){
        this.filepath = filepath;
        this.pathVerification = pathVerification;
        logger.debug("The filepath given", filepath);
    }

    //getter for the filepath/encapsulating it
    public String getFilePath(){
        return filepath;
    }

    //getter for verification checker
    public boolean getPathVerification(){
        return pathVerification;
    }
    
    //getter for the path entered through the command line to verify
    public String getPath(){
        return path;
    }

    //method to start the game
    public void startGame(){

        //creates a new instance of the maze object and calls a function to create the maze
        Maze maze = new Maze();
        maze.createMaze(getFilePath());

        if(getPathVerification()){
            Analyzer analyze = new Analyzer();
        }
    }
}