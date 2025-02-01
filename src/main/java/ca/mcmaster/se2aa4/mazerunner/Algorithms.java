/*
 * Sameer Khichi
 * MacID: khichis student#: 400518172
 * 2AA4 - Assignment 1 - Maze Runner 
 */

package ca.mcmaster.se2aa4.mazerunner;

//this is an interface that all the algorithm classes will implement
//to implement an algorithm make a class extend Analyzer implements Algorithms
interface Algorithms {
    //all algorithms must have a method to compute a path
    public void computePath(char[][] maze, int[] entry, int[] exit);
}
