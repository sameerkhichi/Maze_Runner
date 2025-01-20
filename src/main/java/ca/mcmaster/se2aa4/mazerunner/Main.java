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

    private static final Logger logger = LogManager.getLogger();
    private static Options options = new Options();
    private static CommandLineParser parser = new DefaultParser();
    

    public static void main(String[] args) {

        options.addOption("i", false, "Include maze file path");

        for(int i=0; i < args.length; i++){
            
            logger.debug("Checks flags", args[i]);
            if(options.hasOption("-i")){

                logger.info("** Starting Maze Runner");
                try {

                    logger.info("**** Reading the maze from file " + args[i+1]);
                    BufferedReader reader = new BufferedReader(new FileReader(args[i+1]));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        for (int idx = 0; idx < line.length(); idx++) {
                            if (line.charAt(idx) == '#') {
                                logger.trace("WALL ");
                            } else if (line.charAt(idx) == ' ') {
                                logger.trace("PASS ");
                            }
                        }

                        logger.trace(System.lineSeparator());
                    }

                    reader.close(); //closing the reader

                    break;
                } 

                catch(Exception e){
                    logger.error("/!\\ An error has occured /!\\");
                }
                
            }
        }

        

        logger.info("**** Computing path");
        logger.info("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}
