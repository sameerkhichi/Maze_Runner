/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.parser.ParseException;
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
        options.addOption("i", true, "Maze File - Specifies the filename to be used");
        options.addOption("p", true, "Path sequence - Activates path verification mode to check if the entered sequence solves the maze");

        try{

            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            String filepath;
            String path;
            boolean pathVerification = false;

            if(cmd.hasOption("i")){
                filepath = cmd.getOptionValue("i");
                path = null;

                if(cmd.hasOption("p")){
                    pathVerification = true;
                    path = cmd.getOptionValue("p");
                }

                logger.debug(path);
                Main main = new Main(filepath, pathVerification, path);
                main.startGame();
            }
            else{
                throw new ParseException("To pass a file include -i flag");
            }
        }
        catch(Exception e){
            logger.debug(e);
            logger.error("/!\\ An error has occurred /!\\");
        }
    }

    //constructor for the main class
    public Main(String filepath, boolean pathVerification, String path){
        this.filepath = filepath;
        this.pathVerification = pathVerification;
        this.path = path;
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
        //creating a new analyzer
        Analyzer analyze = new Analyzer();

        if(getPathVerification()){
            logger.info("Verifying given path");
            logger.debug(getPath());
            //printing to standard output if path is correct
            if(analyze.validatePath(maze.getMaze(), maze.getEntry(), maze.getExit(), getPath())){
                System.out.println("correct path");
                System.exit(0);
            }
            else{
                System.out.println("incorrect path");
                System.exit(0);
            }
        }
        else{
            analyze.computePath();
            System.out.println(analyze.getComputedPath());
        }

        //end the program
        logger.info("End of MazeRunner");
        System.exit(0);
    }
}