package jmine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CGFrame extends JFrame {
    ExecutorService runner= Executors.newFixedThreadPool(1);
    JLabel ti=new JLabel("Time");
    time t=new time(ti);

    //global variables
    private ColoredGraph myBoard;
    private String username="";
    private List pPanel;
    private JPanel west;
    private int connected;
    //Top Menu requirement
    private JMenuBar menuBar = new JMenuBar();

    public CGFrame(String username, int nmin, int r, int c, int color,int connected) {

        this.username=username;
        this.connected=connected;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(c*15+296, r*15+50); //Main Frame of the game include in Buttons and 67 is the size statusbar and JMenuBar
        setResizable(false);
        setLocationRelativeTo(null);

        //map
            myBoard = new ColoredGraph(username, nmin, r, c, t, color,connected);
        //Top Menu

        //one
        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem newGame = new JMenuItem("New Game");
        file.add(newGame);
        newGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.newGame();
                myBoard.repaint();
                pPanelCreator(false);
                t.restart();
                runner.execute(t);
            }
        });
        JMenuItem mainMenu = new JMenuItem("Main Menu");
        file.add(mainMenu);
        mainMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                setVisible(false);
                login b2m = new login(1);
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        //two
        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);
        JMenuItem undo = new JMenuItem("undo");
        edit.add(undo);
        undo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                myBoard.undo();
                t.continueT();
                runner.execute(t);
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
                pPanelCreator(true);
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

        //three
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
        //end of Top menu

        //page config
        setTitle("Soldier "+ username);
        add(menuBar, BorderLayout.NORTH);
        myBoard.setPreferredSize(new Dimension(c*15,r*15));
        add(myBoard, BorderLayout.WEST);

        //add pairsPanel
        pPanel=new List(connected,false);

        pPanel.setForeground(Color.BLACK);
        pPanel.setBackground(Color.LIGHT_GRAY);
        pPanel.setFont(new Font("Arial",Font.PLAIN,9));
        pPanel.add("Pairs:(Row,Col)");
        for(int k=0;k<connected;k++)
            pPanel.add(myBoard.getPair(k,false));
        //Create containing panel
        west=new JPanel();
        west.setLayout(new GridLayout(1,1));
        west.setPreferredSize(new Dimension(140, 100));
        west.setSize(new Dimension(140,r*15));
        west.add( pPanel,0);
        //add containing east panel to page
        add(west,BorderLayout.EAST);

        //east buttons
        //Index=1
        JButton b1=new JButton("New Game");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myBoard.newGame();
                myBoard.repaint();
                pPanelCreator(false);
                t.restart();
                runner.execute(t);
            }
        });

        //Index=2

        //Index=3
        JButton b3=new JButton("Solve");
        b3.setBackground(Color.yellow);
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myBoard.solve();
                pPanelCreator(true);
                t.stop();
            }
        });

        //Index=4
        JButton b4=new JButton("Exit");
        b4.setBackground(Color.RED);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        //Create containing panel
        JPanel east=new JPanel();
        east.setLayout(new GridLayout(4, 1, 30, 0));

        //Index=0
        Border border = BorderFactory.createLineBorder(Color.GREEN,4);
        ti.setBorder(border);
        ti.setPreferredSize(new Dimension(150, r*15)); // button panel size because of the timer size
        ti.setHorizontalAlignment(JLabel.CENTER);
        ti.setVerticalAlignment(JLabel.CENTER);
        runner.execute(t);

        //add other buttons we create
        east.add(ti,0);
        east.add(b1,1);
        east.add(b3,2);
        east.add(b4,3);

        //add containing east panel to page
        add(east,BorderLayout.CENTER);
    }

    private void pPanelCreator(boolean show){
        west.remove(pPanel);//remove last item from west
        pPanel.removeAll();//delete list items
        pPanel.add("Pairs:(Row,Col)");
        for(int k=0;k<connected;k++)
            pPanel.add(myBoard.getPair(k,show));
        west.add( pPanel,0);
    }

    private static void exit(){
        System.exit(0);
    }
}
