/* 
 * Represents an N-by-N board with integers 1 through N-1. Zero represents an
 * empty tile.
 */

import java.util.Arrays;

public class Board {

    private final int[] tiles;
    private final int N;

    // construct a board from an N-by-N array of blocks
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N*N];
        int k = 0;
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
                this.tiles[k++] = tiles[i][j];
    }
    
    private Board(int[] tiles) {
        N = (int) Math.sqrt((double) tiles.length);
        this.tiles = new int[N*N];
        for (int i = 0; i < tiles.length; i++) 
            this.tiles[i] = tiles[i];
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {
        int goal = 1;
        int sum = 0;
        for (int i = 0; i < N*N-1; i++)
            if (tiles[i] != goal++)
                sum++;
        return sum;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int goal = 1;
        int sum = 0;
        for (int i = 0; i < N*N-1; i++) {
            if (tiles[i] != goal)
                sum += getMDist(i, goal);
            goal++;
        }
        return sum;
    }

    // get Manhattan distance
    private int getMDist(int i, int goal) {
        int vert = 0;
        int horiz = 0;
        for (int j = 0; j < N*N; j++)
            if (tiles[j] == goal) {
                vert = Math.abs(j/N - i/N);
                horiz = Math.abs(j%N - i%N);
                break;
            }
        return vert + horiz;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (this.manhattan() == 0)
            return true;
        return false;
    }           

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[] twin = Arrays.copyOf(tiles, tiles.length);
        for (int i = 0; i < twin.length; i += N) {
            for (int j = 0; j < N-1; j++) 
                if (twin[i+j] != 0 && twin[i+j+1] != 0) {
                    int old = twin[i+j];
                    twin[i+j] = twin[i+j+1];
                    twin[i+j+1] = old;
                    return new Board(twin);
                }
        }
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension())
            return false;
        for (int i = 0; i < that.tiles.length; i++)
            if (that.tiles[i] != this.tiles[i])
                return false;
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();
        for (int i = 0; i < N*N; i++)
            if (tiles[i] == 0) {
                if (i > 0 && i/N == (i-1)/N) {
                    int[] c = Arrays.copyOf(tiles, tiles.length);
                    c[i] = c[i-1];
                    c[i-1] = 0;
                    neighbors.enqueue(new Board(c));
                }
                if (i-N >= 0) {
                    int[] c = Arrays.copyOf(tiles, tiles.length);
                    c[i] = c[i-N];
                    c[i-N] = 0;
                    neighbors.enqueue(new Board(c));
                }
                if (i < tiles.length-1 && i/N == (i+1)/N) {
                    int[] c = Arrays.copyOf(tiles, tiles.length);
                    c[i] = c[i+1];
                    c[i+1] = 0;
                    neighbors.enqueue(new Board(c));
                }
                if (i+N < tiles.length) {
                    int[] c = Arrays.copyOf(tiles, tiles.length);
                    c[i] = c[i+N];
                    c[i+N] = 0;
                    neighbors.enqueue(new Board(c));
                }
            }
        return neighbors;
    }

    // string representation of the board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N*N; i += N) {
            for (int j = 0; j < N; j++)
                s.append(String.format("%2d ", tiles[i+j]));
            s.append("\n");
        }
        return s.toString();
    }          
    
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tiles[i][j] = in.readShort();
        Board initial = new Board(tiles);
        
        System.out.println("Initial board:");
        System.out.println(initial.toString());

        System.out.println("Hamming = " + initial.hamming());
        System.out.println("Manhattan = " + initial.manhattan());
        System.out.println("Goal board? " + initial.isGoal() + "\n");
        
        Board twin = initial.twin();
        System.out.println("Twin board:");
        System.out.println(twin.toString());
        
        Iterable<Board> neighbors = initial.neighbors();
        System.out.println("Neighbors: ");
        for (Board n : neighbors)
            System.out.println(n.toString());
    }
}