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

public class Analyzer  {

    private final Logger analyzerLogger = LogManager.getLogger();

    private String computedPath;

    //getter for the path computed by the computer
    public String getComputedPath(){
        return computedPath;
    }

    //setter to update the computed path attribute
    public void setComputedPath(String computedPath){
        this.computedPath = computedPath;
    }

    //helper method to check if the move made in the algorithm is valid
    private boolean moveChecker(char[][] maze, int row, int col){
        return row < maze.length && row >=0 && col < maze[0].length && col >= 0 && maze[row][col] != '#';
    }

    //function to compute a path through the maze given (uses the right hand exploration technique)
    public void computePath(char[][] maze, int[] entry, int[] exit){
        analyzerLogger.info("Computing path");

        StringBuilder buildingPath = new StringBuilder();

        int row = entry[0];
        int col = entry[1];
        
        //the method for tracking directions and moves is from the validatePath method
        //start facing east
        int directionFacing = 1;

        //moves to 'walk' across the maze {north, east, south, west}
        int[][] moves = {{-1,0}, {0,1}, {1,0}, {0,-1}};

        while((row != exit[0]) || (col != exit[1])){

            analyzerLogger.debug(buildingPath.toString());

            //checking if the next spot forward is a match for the exit
            if((row + moves[directionFacing][0] == exit[0]) && (col + moves[directionFacing][1] == exit[1])){
                buildingPath.append("F");
                break;
            }

            //storing all variables relating to the right side of the pointer walking through the maze
            int rightSide = ((directionFacing + 1) % 4);
            int rightRow = (row + moves[rightSide][0]);
            int rightCol = (col + moves[rightSide][1]);

            //saving all the variables related to the left side of the pointer
            int leftside = ((directionFacing + 3) % 4);
            int leftRow = (row + moves[leftside][0]);
            int leftCol = (col + moves[leftside][1]);

            //prioritizes turning right - if it can it also moves forward
            //continue at the end to skip the rest of the loop after already moving
            if(moveChecker(maze, rightRow, rightCol)){
                directionFacing = rightSide;
                row = rightRow;
                col = rightCol;
                buildingPath.append("RF");
                continue;
            }
            //second priority is moving forward if right is blocked
            //moving in the direction the pointer is facing
            if(moveChecker(maze, row + moves[directionFacing][0], col + moves[directionFacing][1])){
                row += moves[directionFacing][0];
                col += moves[directionFacing][1];
                buildingPath.append("F");
                continue;
            }
            //if the algorithm cant turn right or go forward it tries turning left and moving forward
            if(moveChecker(maze, leftRow, leftCol)){
                directionFacing = leftside;
                row = leftRow;
                col = leftCol;
                buildingPath.append("LF");
                continue;
            }
            //if it cant turn left then its at a dead end so it turns around fully and moves forward.
            else{
            
                directionFacing = ((directionFacing + 2) % 4);

                row += moves[directionFacing][0];
                col += moves[directionFacing][1];

                //this can be LLF too it doesnt really matter - defaulted to turning right twice                    
                buildingPath.append("RRF");
                continue;
     
            }

        }

        if(validatePath(maze, entry, exit, buildingPath.toString())){
            analyzerLogger.debug("computed path: " + buildingPath.toString());
            setComputedPath(buildingPath.toString());
        }
        else{
            analyzerLogger.debug("Failed Computed Path: " + buildingPath.toString());
            analyzerLogger.error("Right Hand Algorithm Failed");
        }
        
    }

    //this function uses regular expression matches to convert the factorized path to a cononical one
    public String expandPath(String path){

        StringBuilder fullPath = new StringBuilder();

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
            else if(move == 'L'){
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