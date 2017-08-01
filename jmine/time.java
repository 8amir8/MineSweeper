package jmine;

import javax.swing.JLabel;
import java.text.DecimalFormat;

public class time implements Runnable {

  JLabel t;
  int start = 0;
  boolean stoptime = false;
  DecimalFormat df = new DecimalFormat("00");

  public time(JLabel t) {
    this.t = t;
  }

  public void stop() {
    stoptime = true;
    start = 0;
  }
  public void restart() {
    start=0;
    stoptime=false;
  }

  public void run() {
    while (!stoptime) {
      try {
        int m = start / 60;
        int s = start - ((start / 60) * 60);
        String time = df.format(m) + " : " + df.format(s);
        t.setText(time);
        Thread.sleep(1000);
        start++;
      }
      catch (Exception s) {
      }
    }
  }

}
