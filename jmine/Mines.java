package jmine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mines extends JFrame {


    private Board myBoard;
    private String username="";
    private final JLabel statusbar;
    public JMenuBar menuBar = new JMenuBar();

    public Mines(String username,int nmin,int row,int col,int small) {



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(col*15+155, row*15+67); //Main Frame of the game include in Buttons and 67 is the size statusbar and JMenuBar
        setResizable(false);
        setLocationRelativeTo(null);

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);





        myBoard = new Board(statusbar,username,nmin,row,col,t,small);
        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem newGame = new JMenuItem("New Game");
        file.add(newGame);
        newGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.newGame();
                myBoard.repaint();
                t.restart();
                runner.execute(t);
            }
        });
        JMenuItem load = new JMenuItem("Load");
        file.add(load);
        load.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.Load();
            }
        });


        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.Save();
            }
        });

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);

        JMenuItem undo = new JMenuItem("undo");
        edit.add(undo);
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.undo();
            }
        });
        JMenuItem redo = new JMenuItem("redo");
        edit.add(redo);
        redo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.redo();
            }
        });
        JMenuItem solution = new JMenuItem("Solve");
        edit.add(solution);
        solution.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.solve();
                t.stop();
            }
        });

        JMenuItem ladder = new JMenuItem("Ladder");
        edit.add(ladder);
        ladder.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null, new summary().loadallsummaries(), "Soldier's Summary", 1);
            }
        });
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        about.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null, "The player is initially presented with a grid of undifferentiated squares.\n"
                        + "Some randomly selected squares, unknown to the player, are designated to contain mines.\n"
                        +"these mines can be big mines or small mines,\n "
                        +"by walking on 3 small mines or a big mines the player will loose the game ,\n"
                        +"Player is able to Undo and Redo the last move (if he is still in the game),\n"
                        +"player can save and open his/her saved files and will be able to see the ladder of the league,\n"
                        + " Typically, the size of the grid and the number of mines are set in advance by the user,\n"
                        + " either by entering the numbers or selecting from defined skill levels,\n"
                        + " depending on the implementation.\n" +
                        "\n" +
                        "The game is played by revealing squares of the grid by clicking or otherwise indicating each square. \n"
                        + "If a square containing a mine is revealed, the player loses the game. \n"
                        + "If no mine is revealed, a digit is instead displayed in the square, \n"
                        + "indicating how many adjacent squares contain mines;\n"
                        + " if no mines are adjacent, the square becomes blank, \n"
                        + "and all adjacent squares will be recursively revealed.\n"
                        + " The player uses this information to deduce the contents of other squares,\n"
                        + " and may either safely reveal each square or mark the square as containing a mine.\n" +
                        "\n" +
                        "In some versions, a question mark may be placed in an unrevealed square to serve \n"
                        + "as an aid to logical deduction.\n"
                        + " Implementations may also allow players to quickly \"clear around\" \n"
                        + "a revealed square once the correct number of mines have been flagged around it.\n"
                        + " The game is won when all mine-free squares are revealed,\n"
                        + " because all mines have been located.\n"+"\n"
                        + "Thanks again for using Arvin & Shervin Entertainments products.\n"+"For more info about this product call your Persian Friend (REZA).", "About", 1);
            }
        });
        menuBar.add(help);
        setTitle("Soldier "+ username);
        add(menuBar, BorderLayout.NORTH);

        add(myBoard, BorderLayout.CENTER);

        JButton b1=new JButton("New Game");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myBoard.newGame();
                myBoard.repaint();
                t.restart();
                runner.execute(t);
            }
        });
        JButton b2=new JButton("Save");
        b2.setBackground(Color.green);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myBoard.Save();
            }
        });
        JButton b3=new JButton("Solve");
        b3.setBackground(Color.yellow);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myBoard.solve();
                t.stop();
            }
        });
        JButton b4=new JButton("Exit");
        b4.setBackground(Color.RED);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        JPanel east=new JPanel();

        east.setLayout(new GridLayout(5, 1, 30, 0));



        east.add(ti,0);
        Border border = BorderFactory.createLineBorder(Color.RED);
        ti.setBorder(border);
        ti.setPreferredSize(new Dimension(150, 100)); // button panel size because of the timer size


        ti.setHorizontalAlignment(JLabel.CENTER);
        ti.setVerticalAlignment(JLabel.CENTER);
        runner.execute(t);


        east.add(b1,1);
        east.add(b2,2);
        east.add(b3,3);
        east.add(b4,4);


        add(east,BorderLayout.EAST);

    }

    public static void exit(){
        System.exit(0);
    }
    ExecutorService runner=Executors.newFixedThreadPool(1);

    JLabel ti=new JLabel("Time");
    time t=new time(ti);
} 