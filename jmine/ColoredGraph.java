package jmine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoredGraph extends JPanel{
    private final int NUM_IMAGES = 18;
    private final int COVER_FOR_CELL = 17;
    private final int DRAW_COVER = 17;

    private int N_MINES = 15;
    private int N_ROWS = 15;
    private int N_COLS = 15;
    private int N_COLORS = 15;
    private int DRAW_MINE = 0;
    private int DRAW_PAIR = 16;
    private int[][] mine_random;

    private String username;
    private int[] field;
    private boolean inGame;
    private Image[] img;

    private int CELL_SIZE = 15;
    private String G_MODE = "cg";

    private int all_cells;
    private int connected;
    private String[] setPairs;
    private int pair;

    time runner; // reading the timer from time.java class
    boolean solving_mode = false;
    List<int[]> tempfield = new ArrayList<int[]>();// undo and redo field
    int ur = -1, state = 0; // ur is a temp variable to hold the state of the game & State is to hold the position of the game

    public ColoredGraph(String username, int nmin, int r, int c, time run, int colors , int connected) {
        N_COLORS = colors;
        runner = run;
        N_MINES = nmin;
        N_ROWS = r;
        N_COLS = c;
        this.username = username;
        this.connected = connected;
        setPairs=new String[this.connected];

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = (new ImageIcon("./img/cg/" + i + ".png")).getImage();
        }

        setDoubleBuffered(true);
        setOpaque(false);
        addMouseListener(new ColoredGraph.MinesAdapter());
        newGame();
    }

    public void newGame() {
        Random random;

        int i;
        int position1, position2;

        state = 0;
        ur = -1;

        random = new Random();
        inGame = true;

        all_cells = N_ROWS * N_COLS;
        field = new int[all_cells];
        mine_random = connection(all_cells,connected);

        i = 0;
        while (i < N_MINES) {
            position1 = (int) (all_cells * random.nextDouble());
            position2 = (int) (all_cells * random.nextDouble());
            if ((position1 < all_cells) && (position2 < all_cells) && (position1 != position2)) {
                if (mine_random[position1][position2] == 1 && mine_random[position2][position1] == 1) {       //choose mine cells
                    field[position1] = (int) (N_COLORS*random.nextDouble()+1);
                    field[position2] = field[position1];
                    mine_random[position1][position2]++;
                    mine_random[position2][position1]++;
                    i++;
                }
            }
        }

        fillTheMap(0);

        for (i = 0; i < all_cells; i++) {
            field[i] += COVER_FOR_CELL;
            /*System.out.print(i+"-"+field[i]+"-");
            for(int x=0 ; x<all_cells;x++)
                System.out.print(mine_random[i][x]);
            System.out.println("");*/
        }

        int newdest[] = copyarray(field);
        tempfield.add(0, newdest);
    }

    public void solve() {
        solving_mode = true;
        state = 0;                                //disable undo after solve
        ur = -1;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        if (solving_mode) {
            int cell;
            for (int i = 0; i < N_ROWS; i++) {
                for (int j = 0; j < N_COLS; j++) {

                    cell = field[(i * N_COLS) + j];
                    if (checkMine((i * N_COLS) + j)) {           //#MINE#
                        cell = DRAW_MINE;
                    } else if (cell > COVER_FOR_CELL) {
                        cell -= COVER_FOR_CELL;                                             //show mines only
                    }
                    setting(g, i, j, cell);
                }
            }
            solving_mode = false;
            inGame = false;
            //add some solution alarm
            // JOptionPane.showMessageDialog(null,"xx","Solution is ...",1);
            return;
        }
        int cell;
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                cell = field[(i * N_COLS) + j];

                if (!inGame) {                                          //usage: End of the game : uncovering mines and mistakes
                    if (checkMine((i * N_COLS) + j)) {             //#MINE#
                        cell = DRAW_MINE;
                        if (mine_random[(i * N_COLS) + j][pair]==3)//ijkl
                            cell=DRAW_PAIR;
                    }else if (cell > COVER_FOR_CELL) {
                        cell -= DRAW_COVER;
                    }
                }
                else {                                                  //usage: in game repaint
                    if (cell > COVER_FOR_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                setting(g, i, j, cell);
            }
        }

        if (uncover == N_MINES && inGame) {                                   //usage: win reaction
            inGame = false;
            runner.stoptime = true;
            //add some win alarm
            JOptionPane.showMessageDialog(null,"Congratulation! you WoN!","Congrats!",1);
            new summary(G_MODE).savesummary(username, runner.start + "");   //usage : add to ladder
        } else if (!inGame) {
            runner.stoptime = true;
        }
    }

    private boolean isundoEnable() {
        return state > 0;
    }
    private boolean isredoEnable() {
        return state < ur;
    }
    public void undo() {

        if (!isundoEnable()) {
            return;
        }

        if (!inGame) {
            inGame = !inGame;
        }


        field = copyarray(tempfield.get(state - 1));

        state--;
        //System.out.println("Repaint");
        //System.out.println(ur + "\t" + state + "\t" + tempfield.size());
        repaint();

    }
    public void redo() {

        if (!isredoEnable()) {
            return;
        }
        field = copyarray(tempfield.get(state + 1));
        state++;
        //System.out.println("Repaint");
        repaint();
        //System.out.println("REDO\t" + ur + "\t" + state + "\t" + tempfield.size());
    }

    private void setting(Graphics g, int i, int j, int cell) {  //real CELL_SIZE or best output?
        g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);
    }

    class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {


            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
            boolean rep = false;

            if (!inGame) {
                //add some start new game
                JOptionPane.showMessageDialog(null,"Game is Over bro...\n try your chance again","Start a new game",1);
                refresh();
                return;
            }

            if (cCol > N_COLS || cRow > N_ROWS) {
                refresh();
                return;
            }                                     //prevent clicking out of board

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (field[(cRow * N_COLS) + cCol] < COVER_FOR_CELL) {
                    refresh();
                    return;
                }                                 //condition to not increase state when clicking uncovered

                if (e.getButton() == MouseEvent.BUTTON3) {
                    //nothing to do with rclick
                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVER_FOR_CELL) { //or <= COVERED_MINE_CELL
                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        rep = true;

                        if (checkMine((cRow * N_COLS) + cCol)) {
                            //add some game over
                            runner.stoptime=true;
                            JOptionPane.showMessageDialog(null,"Sry mate , take care...\nyou cant respawn in real life!\n#~> Pairs:(" + pair/N_COLS+","+pair%N_COLS+") & ("+cRow+","+cCol+")","Boom! Game Over",2);
                            inGame = false;
                            System.out.println("Mine exploded");
                        }
                    }
                }

                if (rep) {
                    repaint();
                }

            }
            state++;
            int newdest[] = copyarray(field);
            tempfield.add(state, newdest);
            refresh();
            ur = state;
        }
    }

    private void fillTheMap(int i) {
        Random random = new Random();
        if(field[i]==0) {
            int image = (int) (N_COLORS*random.nextDouble()+1);
            field[i] += image;
        }
        for (int j = 0; j < all_cells; j++){
            if (mine_random[i][j] == 1 && field[i]==0) {
                int image = (int) (N_COLORS*random.nextDouble()+1);
                if (image != field[i])
                    field[j]+=image;
            }
        }
        i++;
        if(i<all_cells)
            fillTheMap(i);
    }
    private boolean checkMine(int pos){
        for (int x=0;x<all_cells;x++) {
            if (mine_random[pos][x] == 2) {
                if (mine_random[x][pos] == 2)
                    mine_random[pos][x]++;
                else if (mine_random[x][pos] == 3) {
                    pair=x;
                    return true;
                }
            }
            else if(mine_random[pos][x] == 3){//ijkl
                pair=x;
                return true;
            }
        }
        return false;
    }
    private int[] copyarray(int[] source) {
        int[] dest = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }
    private int[][] connection(int ac,int cn){

        int[][] res=new int[ac][ac];
        for(int c=0; c<ac;c++){
            for (int r=0; r <ac; r++) {
                res[c][r] = 0;
            }
        }
        Random random=new Random();
        int ix = 0;
        while (ix < cn) {
            //System.out.println(ix+"-"+cn);
            int position1 = (int) (all_cells * random.nextDouble());
            int position2 = (int) (all_cells * random.nextDouble());
            if ((position1 < all_cells) && (position2 < all_cells) && (position1 != position2)) {
                if (res[position1][position2] == 0 && res[position2][position1] == 0) {       //choose mine cells
                    res[position2][position1]++;
                    res[position1][position2]++;
                    setPair(position1,position2,ix);
                    ix++;
                }
            }
        }
        /*for(int c=0;c<ac;c++) {
            int x=0;
            for(int h=0;h<ac;h++)
                x+=res[c][h];

            int nc = N_COLORS - x -1;

            while (nc > 0) {
                int r = new Random().nextInt(ac);
                if ((res[c][r] != 1) && (r != c) ) {
                    res[c][r] = 1;
                    res[r][c] = 1;
                    nc--;
                }
            }
        }*/
        return res;
    }
    private void setPair(int a,int b , int index){
        setPairs[index]="#"+(index+1)+":("+a/N_COLS+","+a%N_COLS+") & ("+b/N_COLS+","+b%N_COLS+")";
        //if(mine_random[a/N_COLS][a%N_COLS]==-1&&mine_random[b/N_COLS][b%N_COLS]==-1);
    }
    public String getPair(int index){
        return setPairs[index];
    }
    private void refresh(){
        setVisible(false);
        setVisible(true);
        //System.out.println("Refreshed");
    }
}
