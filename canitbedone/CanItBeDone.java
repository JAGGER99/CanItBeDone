/*
File Name : CanItBeDone
Program Author(s) : Joshua Greene
Program Tester : Micah Hays
Course Number & Title : Data Structures (COSC2203)
Assignment Number & Name : Assignment #5 Can It Be Done?
Due Date : 11/13/2020

Description :  This program gets input from the user that describes a directed
               graph that represents a project with the edges representing the
               time it takes to go from node to node (which represent the stages
               of the project). This program calculates if the project is feasible
               (i.e. no cycles in the graph). It also provides a topological 
               ordering of the nodes(stages) and the total time to complete the
               project. It also calculates the early and late times for completing
               each stage and activity. Lastly it tells the user which activities
               are critical to complete(dont allow for any slack time). 
 */

package canitbedone;

import java.util.*;

public class CanItBeDone {

    public static void main(String[] args) {
       
        final int INFINITY = -1;
        int numOfNodes = 0;
        int numOfEdges = 0;
        int numOfPairs = 0;
        int column = 0;
        boolean possible = false;
        int totalTime = 0;
        
        Scanner input = new Scanner(System.in);
        
        numOfNodes = input.nextInt();
        
        //creating arrays:
        int[] nodes = new int[numOfNodes];
                                          // [rows]     [columns]
        int[][] adjacencyMatrix = new int[numOfNodes][numOfNodes];
        int[] incomingCount = new int[numOfNodes];
        
        //pre-filling 2D array with -1:
        for(int i = 0; i < numOfNodes; i++) {
            for(int j = 0; j < numOfNodes; j++) {
                adjacencyMatrix[i][j] = INFINITY;
            }
        }
        
        //getting rest of input from console:       
        for(int i = 0; i < numOfNodes; i++) {
            
            nodes[i] = input.nextInt();
            numOfPairs = input.nextInt();
            
            for(int j = 0; j < numOfPairs; j++){
                
                //the next input tells the column for the adjacency matrix:
                column = input.nextInt();
                
                /*must subtract 1 from "column" since the "column" values are on
                  a 1 based scale and the array indices are on a 0 based scale.
                */
                adjacencyMatrix[i][column - 1] = input.nextInt();  
                
                //increase counter for number of edges:
                numOfEdges++;
            }        
        }
        
 
        //filling my incomingCount array:
        //for each column:
        for(int j = 0; j < numOfNodes; j++) {
            
           int i;
           
           //for each row:
           for(i = 0; i < numOfNodes; i++) { 
                if(adjacencyMatrix[i][j] > 0) {
                    incomingCount[j]++;
                }
           } 
        }
        
        
        //creating ProjectOperations object:
        ProjectOperations projectO = new ProjectOperations(incomingCount , adjacencyMatrix);
        
        ////////////////////////////////////////////////////////////////////////    
        //CALLING METHODS FROM PROJECT OPERATIONS:
        
                
        //getting the topological order:
        int[] topoOrder = new int[numOfNodes];
        topoOrder = projectO.topologicalOrder(nodes);
       
        //project feasible? 
        possible = projectO.isFeasible();
        
        //early stage time:
        int[] earlyStageTimes = new int[numOfNodes];
        earlyStageTimes = projectO.earlyStage(nodes, topoOrder);
        
        //finding total time to finish the project (the largest value in earlyStageTimes):
        for(int i = 0; i < earlyStageTimes.length; i++) {
            
            if( earlyStageTimes[i] > totalTime) {
                totalTime = earlyStageTimes[i];
            }
        }
      
        
        //late stage time:
        int[] lateStageTimes = new int[numOfNodes];
        lateStageTimes = projectO.lateStage(totalTime, nodes, topoOrder);
        
        //reversing the topological order again after reversing it to get the latestStageTimes above:
        //remember arrays are pass by reference!
        projectO.reverse(topoOrder);
        
        
        //getting early activity times:
        int[] earlyActivityTimes = new int[numOfEdges];
        earlyActivityTimes = projectO.earlyActivities(numOfEdges , numOfNodes , earlyStageTimes);
        
        //getting late activity times:
        int[] lateActivityTimes = new int[numOfEdges];
        lateActivityTimes = projectO.lateActivities(numOfEdges, numOfNodes, lateStageTimes);
        
        //getting critical activities:
        ArrayList<Integer> criticalA = projectO.criticalActivities(earlyActivityTimes, lateActivityTimes);
        
        ////////////////////////////////////////////////////////////////////////
        //OUTPUT:
        
        //outputting feasibility:
        if(possible) {
            System.out.println("The Project is feasible");
        }
        else {
            System.out.println("The Project is NOT feasible");
            System.exit(0);
        }
        
        
        //outputting the topological order:
        System.out.print("\nOrdering: ");
        for(int i = 0; i < topoOrder.length; i++) {
            System.out.print(topoOrder[i] + " ");
        }
        
        
        //outputting early and late stage times :
        
        System.out.println("\n\nStage     Early   Late");
        
        for(int i = 0; i < numOfNodes; i++) {
            
            System.out.println(nodes[i] + "         " + earlyStageTimes[i]
                                        + "       " + lateStageTimes[i] + "\n");
        }
        
        
        //outputting total time:
        System.out.println("Total Project Time: " + totalTime);
        
        
        //outputting early and late activity times:        
        System.out.println("\n\nActivity       Early     Late");
        
        for(int i = 0; i < numOfEdges; i++) {
            
            System.out.println( (i+1) + "              " + earlyActivityTimes[i]
                                        + "         " + lateActivityTimes[i] + "\n");
        }
        
        
        //outputting critical activities:
        System.out.print("Critical Activities: ");
        
        for(int i = 0; i < criticalA.size(); i++) {
                        
            System.out.print(criticalA.get(i));
            
            if(i != criticalA.size()-1) {
                System.out.print(", ");
            }
        }
        
        System.out.println();
        
        
    } //end of main
    
} //end of driver class
