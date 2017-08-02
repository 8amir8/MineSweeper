package jmine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Board extends JPanel {

    public final int NUM_IMAGES = 14;
    public final int CELL_SIZE = 15;
    public final int COVER_FOR_CELL = 10;
    public final int MARK_FOR_CELL = 10;
    public final int EMPTY_CELL = 0;
    public final int MINE_CELL = 9;
    public final int SMALL_CELL = 8;// cell for small mines

    public final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    public final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    public final int DRAW_MINE = 9;
    public final int DRAW_COVER = 10;
    public final int DRAW_MARK = 11;
    public final int DRAW_WRONG_MARK = 12;
    public final int DRAW_SMALL_MINE_CELL = 13; // visable  variable for small mines

    public int N_MINES = 40;
    public int N_SMALL_MINES = 10; // number of small mines
    public int N_ROWS = 16;
    public int N_COLS = 16;

    public String username;
    public int[] field;
    public boolean inGame;
    public int mines_left;
    public Image[] img;

    public int all_cells;
    public JLabel statusbar;
    time runner; // reading the timer from time.java class
    int scounter=0; // counting walking on the small mines
    boolean solving_mode = false;
    List<int[]> tempfield = new ArrayList<int[]>();// undo and redo field
    int ur = -1, state = 0; // ur is a temo variable to hold the state of the game & State is to hold the position of the game  
    int small_mine_random[]=null; //randomly set the small mines in the field variable
    public Board(JLabel statusbar, String username, int nmin, int r, int c, time run,int smallmine) {
        N_SMALL_MINES = smallmine;

        runner = run;
        N_MINES = nmin;
        System.out.println("Small Mine="+N_SMALL_MINES);
        //small_mine_random=random(N_MINES, N_SMALL_MINES);

        N_ROWS = r;
        N_COLS = c;
        this.statusbar = statusbar;
        this.username = username;

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = (new ImageIcon("./img/"+ i + ".png")).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter());
        newGame();

    }

    public void newGame() {

        Random random;
        int current_col;

        int i;
        int position;
        int cell;

        small_mine_random=random(N_MINES, N_SMALL_MINES);
        state=0;

        random = new Random();
        inGame = true;
        mines_left = N_MINES;
        scounter=0;

        all_cells = N_ROWS * N_COLS;
        field = new int[all_cells];

        for (i = 0; i < all_cells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(mines_left)+" with small mine left="+(3-scounter)); //place to change if u want to remove extra flag bug
        i = 0;


        while (i < N_MINES) {

            position = (int) (all_cells * random.nextDouble());

            if ((position < all_cells)
                    && (field[position] != COVERED_MINE_CELL)) {

                current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {                                               //useless?
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < all_cells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }
                cell = position + N_COLS;
                if (cell < all_cells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < all_cells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < all_cells) {                                     //useless?
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }

        int newdest[] = copyarray(field);
        tempfield.add(0, newdest);
    }

    public void solve() {
        solving_mode = true;
        repaint();
    }

    public void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < all_cells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < all_cells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < all_cells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < all_cells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }
    }

    public int position(int c,int r){
        int counter=0;
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                if(i==r && j==c)return counter;
                if(field[i*N_COLS+j]==COVERED_MINE_CELL||field[i*N_COLS+j]== SMALL_CELL||field[i*N_COLS+j]== MINE_CELL )
                {
                    counter++;
                }
            }
        }
        return 0;
    }
    @Override
    public void paintComponent(Graphics g) {

        if (solving_mode) {
            int cell;
            for (int i = 0; i < N_ROWS; i++) {
                for (int j = 0; j < N_COLS; j++) {

                    cell = field[(i * N_COLS) + j];
                    if (cell == COVERED_MINE_CELL||cell==SMALL_CELL) {

                            System.out.println(position(j, i)+"\t"+small_mine_random[position(j, i)]);
                        if(small_mine_random[position(j, i)]==1){// finding array of position of mines and small mines

                            cell=DRAW_SMALL_MINE_CELL;
                        }else{
                            cell = DRAW_MINE;
                        }
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    } else {
                        cell = 10;
                    }

                    g.drawImage(img[cell], (j * CELL_SIZE),
                            (i * CELL_SIZE), this);
                }
            }
            solving_mode = false;
            return;
        }
        int cell;
        int uncover = 0;

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {

                cell = field[(i * N_COLS) + j];

                if (inGame && cell == MINE_CELL) {
                    inGame=false;

                }

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL||cell==SMALL_CELL) {
                        System.out.println(i+":"+j+":"+position(j, i)+"\t"+small_mine_random[position(j, i)]);
                        if(small_mine_random[position(j, i)]==1){

                            cell=DRAW_SMALL_MINE_CELL;
                        }else{
                            cell = DRAW_MINE;
                        }
//                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    } else if(cell==SMALL_CELL) {
                        System.out.println(i+":"+j+":"+position(j, i)+"\t"+small_mine_random[position(j, i)]);


                        cell=DRAW_SMALL_MINE_CELL;
                    }


                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("You are Legend !!!");

            runner.stoptime = true;
            new summary().savesummary(username, runner.start + "");


        } else if (!inGame) {

            runner.stoptime = true;
            new summary().savesummary(username, runner.start + "");
            statusbar.setText("I'm Really Sorry Mate  , you just killed yourself by Walking on mines !!!!");

        }

    }


    public boolean isundoEnable() {
        return state > 0;
    }
    public boolean isredoEnable() {
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
        System.out.println("Repaint");
        System.out.println(ur + "\t" + state + "\t" + tempfield.size());
        repaint();

    }
    public void redo() {

        if (!isredoEnable()) {
            return;
        }
        /*if (state == tempfield.size() - 1) {
            field = copyarray(tempfield.get(state));
            System.out.println("1");
        } else {*/
            field = copyarray(tempfield.get(state + 1));
            System.out.println("2");
        //}
        state++;
        System.out.println("Repaint");
        repaint();
        System.out.println("REDO\t" + ur + "\t" + state + "\t" + tempfield.size());
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

                newGame();
                repaint();
            }

            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
                        rep = true;

                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {
                            if (mines_left > 0) {
                                System.out.println("x = [" + (field[(cRow * N_COLS) + cCol] + MARK_FOR_CELL) + "]");
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                mines_left--;
                                statusbar.setText(Integer.toString(mines_left)+" with small mine left="+(3-scounter));
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else {
                            System.out.println("e = [" + (field[(cRow * N_COLS) + cCol] - MARK_FOR_CELL) + "]");
                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            mines_left++;
                            statusbar.setText(Integer.toString(mines_left)+" with small mine left="+(3-scounter));
                        }
                    }

                } else {

                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
                        System.out.println("wtf");
                        return;
                    }

                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {

                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        rep = true;


                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^");
                            if(small_mine_random[position(cCol, cRow)]==1){
                                field[(cRow * N_COLS) + cCol]=SMALL_CELL;
                                scounter++;
                                statusbar.setText("Hey mate ! Be Careful... you Just walk over "+(scounter)+" small mine/s.");
                                if(scounter==3){/*counting 3 walking steps on the mines */
                                    statusbar.setText("I'm Really Sorry Mate , you walked over small mine for 3rd time!");
                                    inGame = false;
                                }
                            }else{
                                inGame = false;
                            }


                        }
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
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

            ur = state;
        }
    }

    public void Save() {

        BufferedWriter bw = null;
        String mycontent = "";
        for (int i = 0; i < field.length; i++) {
            mycontent += field[i] + " ";
        }
        try {
            System.out.println("Save file: " + username);
            File file = new File("users" +File.separator+ username + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(mycontent);
            System.out.println("File written Successfully");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedWriter" + ex);
            }
        }
    }
    public void Load() {
        BufferedReader br = null;
        String sCurrentLine;
        try {
            br = new BufferedReader(new FileReader("users"+File.separator + username + ".txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String[] vals = sCurrentLine.split(" ");
                int[] savedField = new int[vals.length];
                for (int i = 0; i < savedField.length; i++) {
                    try {
                        savedField[i] = Integer.parseInt(vals[i]);
                    } catch (NumberFormatException e) {
                    }
                }
                field = savedField;
                this.setVisible(false);
                this.setVisible(true);
                System.out.println("File Read Successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public int[] copyarray(int[] source) {
        int[] dest = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }
    public int[] random(int i,int fix){

        int[] res=new int[i];
        for(int c=0;c<i;c++){
            res[c]=0;
        }
        int c=fix;
        while(c>0){
            int pos=new Random().nextInt(i);
            if(res[pos]!=1)
            {
                res[pos]=1;
                c--;
            }
        }
        return res;
    }
}
