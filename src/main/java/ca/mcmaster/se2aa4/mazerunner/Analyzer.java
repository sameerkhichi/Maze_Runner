/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
class that analyzes mazes given a path either from user or an algorithm.
ability to expand or compress paths for simplification or computation
*/
public class Analyzer {

    private final Logger analyzerLogger = LogManager.getLogger();

    //this was made static so it could be changed after the object was created
    private static String computedPath;

    //setter to update the computed path attribute
    /*
     * now since im setting the attributes value in another class
     * and trying to retrieve it in another one, I have to make the attribute static.
     * This is so the setter actually updates the attribute, and the getter isnt just null.
     * I would rather not make static references but I cant find another way to make this work.
     * Note: non static was causing the final path to be printed as "null"
     */
    public void setComputedPath(String computedPath){
        Analyzer.computedPath = computedPath;
    }

    //getter for the path computed by the computer
    public String getComputedPath(){
        return computedPath;
    }

    //method that converts paths to factorized form for output
    public String compressPath(String path){

        StringBuilder factorizedPath = new StringBuilder();
        //I could use regular expression but using a loop and doing it manually is more flexible
        
        int pathLength = path.length();
        int iterations = 0;

        //iterate through the path
        while(iterations < pathLength){

            char currentCharacter = path.charAt(iterations);
            int counter = 1;

            //if the next character is the same as the current then count how many
            while(((iterations + 1) < pathLength) && (path.charAt(iterations + 1) == currentCharacter)){
                counter++;
                iterations++;
            }

            //append how many if not one and what character
            if(counter > 1){
                factorizedPath.append(counter);
            }
            factorizedPath.append(currentCharacter);
            iterations++;
        }
        analyzerLogger.debug("Factorized Path: " + factorizedPath.toString());
        return factorizedPath.toString();
    }

    //this function uses regular expression matches to convert the factorized path to a cononical one for validation
    public String expandPath(String path){

        StringBuilder fullPath = new StringBuilder();
        //I couldve used a loop to manually do this but regular expression were easier in this case
        //regex pattern \\d+ : one or more digit, then matches exactly one letter F,L or R
        //the brackets () are matching groups which is what the while loop uses matcher.group(1 or 2)
        Pattern pattern = Pattern.compile("(\\d+)([FLR])");
        Matcher matcher = pattern.matcher(path);

        int lastIndex = 0;

        //matcher.find will get the next match in the string for the path
        while(matcher.find()){

            //this handles any non factorized path by adding any text before this matches to the string
            fullPath.append(path.substring(lastIndex, matcher.start()));

            //extracting the number from the regex match the matcher gets
            int factorizedAmount = Integer.parseInt(matcher.group(1));
            //getting the direction from the regex match
            char factorizedDirection = matcher.group(2).charAt(0);

            //adding the right direction the same number of times as the value of the number before it
            fullPath.append(String.valueOf(factorizedDirection).repeat(factorizedAmount));

            lastIndex = matcher.end();

        }

        fullPath.append(path.substring(lastIndex));
        analyzerLogger.debug(fullPath.toString());
        return fullPath.toString();
    }


    //function to validate paths given to the program
    public boolean validatePath(char[][] maze, int[] entry, int[] exit, String path){
        
        analyzerLogger.debug("Path before validation: " + path);

        //retrieving starting position from entry array
        int row = entry[0];
        int col = entry[1];

        //this is assuming you start on the east and end on the west
        //0, 1, 2, 3 = north, east, south, west
        int directionFacing = 1;
        
        //moves to 'walk' across the maze {north, east, south, west}
        int[][] moves = {{-1,0},{0,1},{1,0},{0,-1}};

        int movementCount = 0;
        while(movementCount < path.length()){
            char move = path.charAt(movementCount);

            //turn right
            if(move == 'R'){
                directionFacing = ((directionFacing + 1) % 4);
            }
            //turn left
            else if(move == 'L'){ //these NEED to be else if, otherwise it breaks everything, took me 2 hours of yelling to figure that out
                directionFacing = ((directionFacing + 3) % 4);
            }
            //ignore spaces
            else if(move == ' '){
                continue;
            }
            //move forward
            else if(move == 'F'){
                int nextRow = row + moves[directionFacing][0];
                int nextCol = col + moves[directionFacing][1];

                //checking if any move sends the player out of the maze
                if(nextCol < 0 || nextCol >= maze[0].length || nextRow < 0 || nextRow >= maze.length){
                    //ensuring the maze is solved by comparing current position with exit position
                    if((nextRow == exit[0]) && (nextCol == exit[1])){
                        analyzerLogger.info("Valid path given");
                        return true;
                    }
                    analyzerLogger.info("Out of Bounds!");
                    return false;
                }

                //checking to see if the player hits a wall
                if(maze[nextRow][nextCol] == '#'){
                    analyzerLogger.info("You hit a wall!");
                    analyzerLogger.debug(nextRow);
                    analyzerLogger.debug(nextCol);
                    return false;
                }

                //updating the rows and columns with ones after moving
                col = nextCol;
                row = nextRow;
            }
            else{
                analyzerLogger.debug("Illegal move", move);
                throw new IllegalArgumentException("Invalid move: " + move);
            }
            movementCount++;
        }
        
        //if the entered path is not illegal but does not solve the maze
        analyzerLogger.info("Invalid path given - Still inside maze");
        return false;
    
    }
}