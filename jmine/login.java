package jmine;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.sqrt;

public class login {
    JFrame jf = new JFrame("Mine Game");
    JPanel m = new JPanel(new GridLayout(6, 3, 5, 5));
    String username = "default";
    int nmin = 30, r = 16, c = 16,s=3;
    JComboBox jc;
    JSlider jp1, jp2, jp3, jp4 ,jp5;
    JLabel jl1, jl2, jl3, jl4,jl5;
    int num_min=225;
    int cn=45;

    public login(){
        jf.setTitle("Enter to MineSweeper");
        jf.setSize(350, 300);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        //phase-1
        Object[] options = {"I Agree", "I don't Agree!"};
        int n = JOptionPane.showOptionDialog(null,
                "Arvin & Shervin Entertainment is not responsible for any damages caused by explosions",
                "A & S Entertainment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options,
                options[1]);
        //phase-2
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
        //phase-3
        Object[] modes = {"Square", "Hex" , "ColoredGraph"};
        int nn = JOptionPane.showOptionDialog(null,
                "Choose the game mode you want to play",
                "Game mode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                modes,
                modes[1]);
        if (nn == JOptionPane.YES_OPTION) {
            menu("square");
        }
        else if(nn==JOptionPane.NO_OPTION){
            menu("hexagon");
        }
        else{
            menu("cg");
        }
        //start
        jf.add(m);
        jf.setVisible(true);
    }

    public void menu(String mode ) {
        JLabel l1 = new JLabel("  No. Rows");
        JLabel l2 = new JLabel("  No. Cols");
        JLabel l3 = new JLabel("  No. allMines");
        JLabel l4 = new JLabel("  No. sMines");
        JLabel l5 = new JLabel("  Hardship:");

        if(mode=="cg")
        {
            l3.setText("  No. Mines");
            l4.setText("  No. Colors");
            l5.setText("  No. Conns");
        }
        else {
            jc = new JComboBox(new String[]{"Simple", "Intermediate", "Hard"});
            jc.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    jp3.setValue((jc.getSelectedIndex() + 1) * (int) sqrt(num_min));
                    jp4.setValue((jc.getSelectedIndex() + 1) * (int) sqrt(num_min) / 3);
                }
            });
        }
        //line 1
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
                if(mode=="cg")
                {
                    jl3.setText("1 : " + jp3.getValue() + " : " + num_min/2);
                    jp3.setMaximum(num_min/2);
                    jl4.setText("1 : " + jp4.getValue() + " : " + Math.min(jp3.getValue(),15));
                    jp4.setMaximum(Math.min(jp3.getValue(),15));                                 //max colors we support
                    num_min = jp1.getValue() * jp2.getValue();
                    jl5.setText(jp3.getValue()+" : " + jp5.getValue() + " : " + num_min/2);
                    jp5.setMaximum(num_min/2);
                    jp5.setMinimum(jp3.getValue());
                }
            }
        });

        //line 2
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
                if(mode=="cg")
                {
                    jl3.setText("1 : " + jp3.getValue() + " : " + num_min/2);
                    jp3.setMaximum(num_min/2);
                    jl4.setText("1 : " + jp4.getValue() + " : " + Math.min(jp3.getValue(),15));
                    jp4.setMaximum(Math.min(jp3.getValue(),15));                                 //max colors we support
                    num_min = jp1.getValue() * jp2.getValue();
                    jl5.setText(jp3.getValue()+" : " + jp5.getValue() + " : " + num_min/2);
                    jp5.setMaximum(num_min/2);
                    jp5.setMinimum(jp3.getValue());                }
            }
        });

        //line 3
        jp3 = new JSlider(1, num_min, 15);
        jp3.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jl3.setText("1 : " + jp3.getValue() + " : " + num_min);
                jl4.setText("1 : " + jp4.getValue() + " : " + jp3.getValue());
                jp4.setMaximum(jp3.getValue());
                if(mode=="cg")
                {
                    jl3.setText("1 : " + jp3.getValue() + " : " + num_min/2);
                    jp3.setMaximum(num_min/2);
                    jl4.setText("1 : " + jp4.getValue() + " : " + Math.min(jp3.getValue(),15));
                    jp4.setMaximum(Math.min(jp3.getValue(),15));                                 //max colors we support
                    num_min = jp1.getValue() * jp2.getValue();
                    jl5.setText(jp3.getValue()+" : " + jp5.getValue() + " : " + num_min/2);
                    jp5.setMaximum(num_min/2);
                    jp5.setMinimum(jp3.getValue());
                }
            }
        });

        //line 4
        jp4 = new JSlider(1, 15, 5);
        jp4.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jl4.setText("1 : " + jp4.getValue() + " : " + jp3.getValue());
                if(mode=="cg")
                {
                    jl4.setText("1 : " + jp4.getValue() + " : " + Math.min(jp3.getValue(),15));
                    jp4.setMaximum(Math.min(jp3.getValue(),15));                                 //max colors we support
                }
            }
        });

        //line5
        if(mode=="cg") {
            jp5 = new JSlider(jp3.getValue(), jp1.getValue() * jp2.getValue()/2, 45);
            jp5.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    num_min = jp1.getValue() * jp2.getValue();
                    jl5.setText(jp3.getValue()+" : " + jp5.getValue() + " : " + num_min/2);
                    jp5.setMaximum(num_min/2);
                    jp5.setMinimum(jp3.getValue());
                }
            });
        }
        //start button
        JButton jb = new JButton("Start!");
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mapGenerator(mode);
            }
        });

        jl1 = new JLabel("5 : 15 : 30");
        jl2 = new JLabel("5 : 15 : 30");
        jl3 = new JLabel("1 : 15 : " + num_min);
        jl4 = new JLabel("1 : 5 : 15");
        jl5 = new JLabel("15 : 45 : "+ num_min/2);

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
        if(mode=="cg"){
            m.add(jp5,13);
            m.add(jl5,14);
        }
        else{
            m.add(jc, 13);
            m.add(new JLabel(), 14);
        }

        m.add(new JLabel(), 15);
        m.add(jb, 16);
        m.add(new JLabel(), 17);
    }

    //generating map
    public void mapGenerator(String mode) {
        this.jf.setVisible(false);
        JFrame m;
        r = jp1.getValue();
        c = jp2.getValue();
        nmin = jp3.getValue();
        s = jp4.getValue();
        cn=jp5.getValue();
        if(mode=="cg")
            m = new CGFrame(username, nmin, r, c,s,cn);
        else
            m = new Mines(username, nmin, r, c,s,mode);
        m.setVisible(true);
    }
    //Test
    public static void main(String a[]) {
        login mnm = new login();

    }
}
