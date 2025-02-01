/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//class that implements the right hand algorithm
public class RightHand extends Analyzer implements Algorithms{

    private final Logger righthandLogger = LogManager.getLogger();

    //helper method to check if the move made in the algorithm is valid
    private boolean moveChecker(char[][] maze, int row, int col){
        return row < maze.length && row >=0 && col < maze[0].length && col >= 0 && maze[row][col] != '#';
    }

    //this method is overrides the interface method
    //function to compute a path through the maze given (uses the right hand exploration technique)
    public void computePath(char[][] maze, int[] entry, int[] exit){
        righthandLogger.info("Computing path");

        StringBuilder buildingPath = new StringBuilder();

        int row = entry[0];
        int col = entry[1];
        
        //the method for tracking directions and moves is from the validatePath method
        //start facing east
        int directionFacing = 1;

        //moves to 'walk' across the maze {north, east, south, west}
        int[][] moves = {{-1,0}, {0,1}, {1,0}, {0,-1}};

        while((row != exit[0]) || (col != exit[1])){

            righthandLogger.debug(buildingPath.toString());

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
            righthandLogger.debug("computed path: " + buildingPath.toString());
            //the path to be printed is in factorized format for simplicity 
            setComputedPath(compressPath(buildingPath.toString()));
        }
        else{
            righthandLogger.debug("Failed Computed Path: " + buildingPath.toString());
            righthandLogger.error("Right Hand Algorithm Failed");
        }
        
    }
}