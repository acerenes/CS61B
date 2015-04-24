import java.util.Observable;

import java.util.LinkedList;
/** 
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int x;
    private int y;
    private int s; // Source vertex number. 
    private int t; // Target vertex number. 
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);   

        this.x = targetX;
        this.y = targetY;
        this.s = m.xyTo1D(sourceX, sourceY); // Returns the vertex number. 
        this.t = m.xyTo1D(targetX, targetY);

        this.maze = m;

        distTo[s] = 0; 
        edgeTo[s] = s; // I guess you're your own parent. 
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int s) {

        marked[s] = true;
        announce();
        // Don't worry about edgeTo for s. Because s is the source, and when setting up the mazeExplorer, we already took care of that. 

        if (s == this.t) {
            // In case found it already. 
            return;
        }
        
        // Storing the vertices to look at = ints. 
        LinkedList<Integer> q = new LinkedList<Integer>(); 

        q.add(s);

        while (!q.isEmpty()) {
            int parent = q.poll();
            // Then do "holy trinity" for its children. 
            for (int child : this.maze.adj(parent)) {
                // But have to make sure haven't visited yet.
                if (!marked[child]) {
                    marked[child] = true; // MARK
                    q.add(child); // ENQUEUE
                    edgeTo[child] = parent; // SET EDGE TO

                    distTo[child] = distTo[parent] + 1;
                    
                    announce();
                    
                    if (child == this.t) {
                        // Found final position!
                        return;
                    }
                }
            }
        }

    }


    @Override
    public void solve() {
        bfs(s);
    }
} 

