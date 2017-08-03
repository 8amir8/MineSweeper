package jmine;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HexBoard extends Board {
    public HexBoard(JLabel statusbar, String username, int nmin, int r, int c, time run, int smallmine) {
        super(statusbar, username, nmin, r, c, run, smallmine);

        img = new Image[NUM_IMAGES];
        CELL_SIZE = 24;
        G_MODE="hexagon";
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = (new ImageIcon("./img/HeX2/"+ i + ".png")).getImage();
        }

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
                    if(current_col%2==1) {
                        cell = position - 1 - N_COLS;                                   //odd
                        if (cell >= 0) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                    }
                        cell = position - 1;
                        if (cell >= 0) {                                                //useless?
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                    if(current_col%2==0) {                                              //even
                        cell = position + N_COLS - 1;
                        if (cell < all_cells) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
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
                    if(current_col%2==1) {                                              //odd
                        cell = position - N_COLS + 1;
                        if (cell >= 0) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                    }
                    if(current_col%2==0) {                                              //even
                        cell = position + N_COLS + 1;
                        if (cell < all_cells) {
                            if (field[cell] != COVERED_MINE_CELL) {
                                field[cell] += 1;
                            }
                        }
                    }
                        cell = position + 1;
                        if (cell < all_cells) {                                         //useless?
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
    public void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            if(current_col%2==1) {
                cell = j - N_COLS - 1;
                if (cell >= 0) {
                    if (field[cell] > MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        if (field[cell] == EMPTY_CELL) {
                            find_empty_cells(cell);
                        }
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL; //to ensure that it would not clear ur flags
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
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
                    }
                }
            if(current_col%2==0) {
                cell = j + N_COLS - 1;
                if (cell < all_cells) {
                    if (field[cell] > MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        if (field[cell] == EMPTY_CELL) {
                            find_empty_cells(cell);
                        }
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
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
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
                    }
                }

                cell = j + N_COLS;
                if (cell < all_cells) {
                    if (field[cell] > MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        if (field[cell] == EMPTY_CELL) {
                            find_empty_cells(cell);
                        }
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
                    }
                }

        if (current_col < (N_COLS - 1)) {
            if(current_col%2==1) {
                cell = j - N_COLS + 1;
                if (cell >= 0) {
                    if (field[cell] > MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        if (field[cell] == EMPTY_CELL) {
                            find_empty_cells(cell);
                        }
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
                    }
                }
            }
            if(current_col%2==0) {
                cell = j + N_COLS + 1;
                if (cell < all_cells) {
                    if (field[cell] > MINE_CELL) {
                        field[cell] -= COVER_FOR_CELL;
                        if (field[cell] == EMPTY_CELL) {
                            find_empty_cells(cell);
                        }
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
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
                        else if(field [cell] > MINE_CELL) field[cell] += COVER_FOR_CELL;
                    }
                }
        }
    }

}
