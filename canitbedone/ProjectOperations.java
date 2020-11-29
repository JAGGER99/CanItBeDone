/*
 This class contains all the methods for gathering the following information:
  - topological order
  - feasibility
  - early & late stage times
  - early & late activity times
  - reverse order of an array 
 */
package canitbedone;

import java.util.ArrayList;
import java.util.Stack;

//the class:
public class ProjectOperations {
    
    boolean feasible;
    int[] inCount;
    int[][] adjMatrix;
    Stack<Integer> myStack = new Stack<>();
    
    
    //constructor:
    public ProjectOperations(int[] inCount, int[][] adjMatrix) {
        feasible = false;
        this.inCount = inCount;
        this.adjMatrix = adjMatrix;
    }
    
    //methods:
    ///////////////////////////////////////////////////////////////////////////
    //topologicalOrder method:
    public int[] topologicalOrder(int[] nodes) {
        
        int top = 0;
        int counter = 0;
        int[] topo = new int[nodes.length];
        
        for(int i = 0; i < inCount.length; i++) {
            
            if(inCount[i] == 0) {
                myStack.push(i);
            } 
        }
        
        while(!myStack.isEmpty()) {
            
            top = myStack.pop();
            topo[counter] = nodes[top];
            
            for(int j = 0; j < adjMatrix[top].length; j++) {
                
                if(adjMatrix[top][j] > 0) {
                    
                    inCount[j]--;
                    
                    if(inCount[j] == 0) {
                        
                        myStack.push(j);
                    }
                }
            }
            counter++;
        }         
        
              
        if(topo.length == nodes.length) {
            feasible = true;
        }
        
        return topo;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    //isFeasible method:
    boolean isFeasible() {
        return feasible;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    //earlyStage method:
    public int[] earlyStage(int[] nodes , int[] topological) {
        
      
        //array for holding all early stage times:
        int[] earlyS = new int[nodes.length];
              
        //array for holding all possible paths to the next node:
        int[] paths = new int[nodes.length];       
        
        int index = 0;
        int max = 0;
        
        //loop through for each column of adjacency matrix:     
        for(int n = 0; n < nodes.length; n++) {
            
            //loop through all the rows of adjacency matrix:
            for(int r = 0; r < nodes.length; r++) {
                
                //if there is a path:
                if(adjMatrix[r][topological[n] - 1] > 0) {
                    
                    paths[index] = earlyS[r] + adjMatrix[r][topological[n] - 1];  
                    index++;
                }  
            }
            
            //determining the maximum cost path:
            for(int k = 0; k < index; k++) {
                if(paths[k] > max) {
                    max = paths[k];
                }
            }
            
            //setting early stage time (at index determined by the topological order) to "max":
            earlyS[topological[n] - 1] = max;
            
            //reset index and max to 0 for next column:
            index = 0;
            max = 0;
        }  
        
        return earlyS;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////
    //lateStage method:
    public int[] lateStage(int totalProjectTime, int[] nodes, int[] topological) {
        
        int[] lateS = new int[nodes.length];
        
        //calling the reverse method on the topological order:
        reverse(topological);        
        
        //array for holding all possible paths to the next node:
        int[] paths = new int[nodes.length];       
        
        int index = 0;
        int min = totalProjectTime;
        
        //loop through for each row of adjacency matrix:     
        for(int n = 0; n < nodes.length; n++) {
                        
            //loop through all the columns of adjacency matrix:
            for(int c = 0; c < nodes.length; c++) {
                
                //if there is a path:
                if(adjMatrix[topological[n] - 1][c] > 0) {
                    
                    paths[index] = lateS[c] - adjMatrix[topological[n] - 1][c];  
                    index++;
                }  
            }
            
            //determining the minimum cost path:
            for(int k = 0; k < index; k++) {
                if(paths[k] < min && paths[k] >= 0) {
                    min = paths[k];
                }
            }
            
            //setting late stage time (at index determined by the topological order) to "min":
            lateS[topological[n] - 1] = min;
            
            //reset index and min to 0 for next column:
            index = 0;
            min = totalProjectTime;
        }  
        
        return lateS;
        
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    //method for reversing the elements of an int array:
    public void reverse(int[] array) {
                    
        for(int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;   
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    //method for getting early activity times:
    public int[] earlyActivities(int numOfActivities , int nodeTotal , int[] earlyStageT) {
        
        int[] earlyA = new int[numOfActivities];
        int counter = 0;
        
        for(int r = 0; r < nodeTotal; r++) {
            for(int c = 0; c < nodeTotal; c++) {
                
                if(adjMatrix[r][c] > 0) {
                    earlyA[counter] = earlyStageT[r];
                    counter++;
                }
                
            }
        }
        return earlyA;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //method for getting late activity times:
    public int[] lateActivities(int numOfActivities, int nodeTotal, int[] lateStageT) {
        
        int[] lateA = new int[numOfActivities];
        int counter = 0;
        
        for(int r = 0; r < nodeTotal; r++) {
            for(int c = 0; c < nodeTotal; c++) {
                
                if(adjMatrix[r][c] > 0) {
                    lateA[counter] = lateStageT[c] - adjMatrix[r][c];
                    counter++;
                }   
            }
        }
        return lateA;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    //method for getting critical activities:
    public ArrayList<Integer> criticalActivities(int[] eat , int[] lat) {
        
        //using arrayList because I don't know how many there are:
        ArrayList<Integer> crit = new ArrayList<>();
        
        for(int i = 0; i < eat.length; i++) {
            if(lat[i] - eat[i] == 0) {
                crit.add(i+1);
            }
        }        
        return crit;
    }
    
   
} //end of class
