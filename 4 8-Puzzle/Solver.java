/* 
 * Solves the 8-puzzle problem (and its natural generalizations) using 
 * the A* search algorithm. 
 */

public class Solver {
    
    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
    private SearchNode min;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {        
        pq.insert(new SearchNode(initial, true));
        pq.insert(new SearchNode(initial.twin(), false));
        
        min = pq.delMin();
        
        while (!min.board.isGoal()) {
            for (Board b : min.board.neighbors())
                if (min.priorSn == null || !b.equals(min.priorSn.board))
                    pq.insert(new SearchNode(min, b));
            min = pq.delMin();
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return min.isOrig;
    }
    
    // min number of moves to solve initial board; -1 if no solution           
    public int moves() {
        if (isSolvable())
            return min.moves;
        else return -1;
    }
    
    // sequence of boards in test solution; null if no solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> seq = new Stack<Board>();  
            seq.push(min.board);
            SearchNode prior = min.priorSn;
            
            while (prior != null) {
                seq.push(prior.board);
                prior = prior.priorSn;
            }                                                                  
            return seq;
        }
        return null;
    }

    // solve a slider puzzle
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readShort();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
    private class SearchNode implements Comparable<SearchNode> {
        SearchNode priorSn = null;
        Board board = null;
        int priorityF = 0;
        int moves = 0;
        boolean isOrig = true;
        
        public SearchNode(Board initial, boolean isOrig) {
            this.moves = 0;
            this.board = initial;
            this.isOrig = isOrig;
            this.priorityF = initial.manhattan();
        }
        
        public SearchNode(SearchNode priorSn, Board board) {
            this.priorSn = priorSn;
            this.board = board;
            this.moves = priorSn.moves+1;
            this.priorityF = board.manhattan() + this.moves;
            this.isOrig = priorSn.isOrig;
        }

        @Override
        public int compareTo(SearchNode sn) {
            if (this.priorityF < sn.priorityF) 
                return -1;
            if (this.priorityF > sn.priorityF) 
                return 1;
            return 0;
        }
    }
}