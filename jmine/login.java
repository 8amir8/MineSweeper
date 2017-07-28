package jmine;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import static java.lang.Math.sqrt;

public class login {
    JFrame jf = new JFrame("Mine Game");
    JPanel m = new JPanel(new GridLayout(6, 3, 5, 5));
    String username = "default";
    int nmin = 40, r = 16, c = 16,s=3;
    JComboBox jc;
    JSlider jp1, jp2, jp3, jp4;
    JLabel jl1, jl2, jl3, jl4;
    int num_min=225;
    public void logstop() {
        this.jf.setVisible(false);
        nmin = jp3.getValue();
        System.out.println(nmin);
        r = jp1.getValue();
        c = jp2.getValue();

        s = jp4.getValue();

        Mines m = new Mines(username, nmin, r, c,s);
        m.setVisible(true);
    }

    public void square() {
        JLabel l1 = new JLabel("  No of Rows");
        JLabel l2 = new JLabel("  No of Columns");
        JLabel l3 = new JLabel("  Number of ALL Mines");
        JLabel l4 = new JLabel("  Number of Small Mines");
        JLabel l5 = new JLabel("  Hardship of Game");
        jc = new JComboBox(new String[]{"Simple", "Intermediate", "Hard"});
        jc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jp3.setValue((jc.getSelectedIndex() + 1) * (int) sqrt(num_min));
                jp4.setValue((jc.getSelectedIndex() + 1) * (int) sqrt(num_min)/3);
            }
        });
        jp1 = new JSlider(5, 30, 15);
        jp1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                num_min = jp1.getValue() * jp2.getValue();
                jl1.setText("5 : " + jp1.getValue() + " : 30");
                jl3.setText("1 : " + jp3.getValue() + " : " + num_min);
                jl4.setText("1 : " + jp4.getValue() + " : " + num_min);
                jp3.setMaximum(num_min);
                jp4.setMaximum(num_min);
            }
        });
        jp2 = new JSlider(5, 30, 15);
        jp2.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                num_min = jp1.getValue() * jp2.getValue();
                jl2.setText("5 : " + jp2.getValue() + " : 30");
                jl3.setText("1 : " + jp3.getValue() + " : " + num_min);
                jl4.setText("1 : " + jp4.getValue() + " : " + num_min);
                jp3.setMaximum(num_min);
                jp4.setMaximum(num_min);
            }
        });
        jp3 = new JSlider(1, num_min, 15);
        jp3.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jl3.setText("1 : " + jp3.getValue() + " : " + num_min);
                jl4.setText("1 : " + jp4.getValue() + " : " + jp3.getValue());
                jp4.setMaximum(jp3.getValue());
            }
        });
        jp4 = new JSlider(1, 15, 5);
        jp4.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jl4.setText("1 : " + jp4.getValue() + " : " + jp3.getValue());
            }
        });
        JButton jb = new JButton("Start Game");
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                logstop();
            }
        });
        jl1 = new JLabel("5 : 15 : 30");
        jl2 = new JLabel("5 : 15 : 30");
        jl3 = new JLabel("1 : 15 : " + num_min);
        jl4 = new JLabel("1 : 5 : 15");
        m.add(l1, 0);
        m.add(jp1, 1);
        m.add(jl1, 2);

        m.add(l2, 3);
        m.add(jp2, 4);
        m.add(jl2, 5);

        m.add(l3, 6);
        m.add(jp3, 7);
        m.add(jl3, 8);

        m.add(l4, 9);
        m.add(jp4, 10);
        m.add(jl4, 11);

        m.add(l5, 12);
        m.add(jc, 13);
        m.add(new JLabel(), 14);
        m.add(new JLabel(), 15);
        m.add(new JLabel(), 16);
        m.add(jb, 17);
    }
    public login(){
        Object[] options = {"I Agree", "I don't Agree!"};

        int n = JOptionPane.showOptionDialog(null,
                "Arvin & Shervin Entertainment is not responsable for any damages caused by explosions",
                "A & S Entertainment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options,
                options[1]);


        String dialogText = "Input Username";

        if (n == JOptionPane.YES_OPTION) {
            do {
                username = JOptionPane.showInputDialog(null, dialogText, null, JOptionPane.INFORMATION_MESSAGE);
                dialogText = "Input valid Username!... Please";
            } while (username.equals(""));
        }
        else{
            System.exit(0);
        }

        //
        Object[] modes = {"Square", "Hex"};

        int nn = JOptionPane.showOptionDialog(null,
                "Choose the game mode you want to play",
                "Game mode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                modes,
                modes[1]);
        if (nn == JOptionPane.YES_OPTION) {
            square();
        }
        else{
            System.out.println("Hex?");
            System.exit(0);
        }
        //

        jf.setTitle("Enter to MineSweeper");

        jf.add(m);
        jf.setSize(500, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    public static void main(String a[]) {
        login mnm = new login();

    }
}
