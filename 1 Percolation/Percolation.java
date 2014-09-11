/*****************************************************************************
 * This class models a percolation system using an N-by-N grid of sites. Each 
 * site is either open or blocked. A full site is an open site that can be 
 * connected to an open site in the top row via a chain of neighboring (left, 
 * right, up, down) open sites. The system percolates if there is a full site 
 * in the bottom row.
 *****************************************************************************/

public class Percolation {
    
    private int N;                        // the number of rows/columns in the grid
    private boolean[][] grid;             // grid represents which sites are open
    private WeightedQuickUnionUF ufFull;  // union-find data structure to determine which sites are full
    private WeightedQuickUnionUF ufPerc;  // determines whether the lattice percolates
    
    /**
     * Initialize an N-by-N grid with all sites blocked.
     * @param size The number of rows/columns in the grid
     * @throw IllegalArgumentException if N is less than 1
     */
    public Percolation(int size) {
        if (size <= 0) 
            throw new IllegalArgumentException("Illegal argument.");
        N = size;
        grid = new boolean[N+1][N+1];              // first row = 1
        ufFull = new WeightedQuickUnionUF(N*N+2); // 1 extra nodes for virtual top
        ufPerc = new WeightedQuickUnionUF(N*N+2); // 2 extra nodes for virtual top/bottom
    }
    
    // get a unique ID for the site (row i, column j)
    private int getID(int i, int j) {
        return (i - 1)*N + j;
    }
    
    // invoke the union method on both instances of the union-find data type
    private void connect(int i, int j) {
        ufFull.union(i, j);
        ufPerc.union(i, j);
    }
    
    /**
     * Opens the specified site.
     * @param i the row of the site to open
     * @param j the column of the site to open
     * @throw IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public void open(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N)
            throw new IndexOutOfBoundsException("Site out of bounds.");
        grid[i][j] = true;
        int id = getID(i, j);   
        if ((i > 1) && isOpen(i-1, j))  // connect site to any open site above
            connect(id, getID(i-1, j));
        if ((j < N) && isOpen(i, j+1))  // to the right
            connect(id, getID(i, j+1));      
        if ((i < N) && isOpen(i+1, j))  // below
            connect(id, getID(i+1, j));
        if ((j > 1) && isOpen(i, j-1))  // to the left
            connect(id, getID(i, j-1));
        
        if (i == 1)                   // connect to the virtual top site
            connect(id, 0);          
        if (i == N)                   // virtual bottom site
            ufPerc.union(id, N*N+1); 
    }
    
    /**
     * Is the site open?
     * @param i the row of the site
     * @param j the column of the site
     * @return whether the site is open
     * @throw IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N)
            throw new IndexOutOfBoundsException("Site out of bounds.");
        return grid[i][j];
    }   
    
    /**
     * Is the site full (i.e., connected to an open site at the top)?
     * @param i the row of the site
     * @param j the column of the site
     * @return whether the system is full
     * @throw IndexOutOfBoundsException if the coordinates are out of bounds
     */
    public boolean isFull(int i, int j) { 
        if (i < 1 || i > N || j < 1 || j > N)
            throw new IndexOutOfBoundsException("Site out of bounds.");
        return ufFull.connected(0, getID(i, j));
    }
    
    /**
     * Does the system percolate?
     * @return whether the system percolates
     */
    public boolean percolates() {
        return ufPerc.connected(0, N*N+1);
    }
}
