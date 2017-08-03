package jmine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class summary {
    private String mode;

    public summary(String mode) { this.mode=mode;}
    public summary(){} //default constructor

    public String loadallsummaries() {
        BufferedReader br = null;
        String sCurrentLine;
        int counter = 1;
        List<String> sort=new ArrayList<String>();
        String result = "Final result of old games:(username,timelaps,mode)\n";
        try {
            br = new BufferedReader(new FileReader("summary.txt"));
            while ((sCurrentLine = br.readLine()) != null) {

                if (sCurrentLine.length() > 0) {

                    sort.add(sCurrentLine);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "File summary.txt not found";
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        String a[]=sorting(sort);
        for(String s:a){
            result+= (counter++ + " ) " + s + "\n");
        }
        return result;
    }
    public String[] sorting(List<String> input){

        Object obj[]=input.toArray();
        String array[]=new String[obj.length];
        int i=0;
        for(Object o:obj){
            array[i++]=""+o;
        }
        for(i=0;i<array.length;i++){
            for(int j=i;j<array.length;j++){

                String[] vi = array[i].split(",");
                String[] vj = array[j].split(",");
                if(Integer.parseInt(vi[1])>Integer.parseInt(vj[1])){
                    String temp=array[i];
                    array[i]=array[j];
                    array[j]=temp;
                }
            }
        }

        return array;
    }
    public int loadonesummary(String username) {
        BufferedReader br = null;
        String sCurrentLine;
        try {
            br = new BufferedReader(new FileReader("summary.txt"));
            int linenumber = 0;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] vals = sCurrentLine.split(",");
                if (vals[0].equals(username)) {
                    return linenumber;
                }
                linenumber++;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                return -1;
            }
        }
        return -1;
    }

    public void savesummary(String username, String rank) {
        int oldvalue = loadonesummary(username);
        String summary = "";

        BufferedReader br = null;
        String sCurrentLine;
        try {
            br = new BufferedReader(new FileReader("summary.txt"));
            int linenumber = 0;
            while ((sCurrentLine = br.readLine()) != null) {

                if (linenumber == oldvalue) {
                    summary += (username + "," + rank + "," + mode +"\n");
                } else {
                    summary += (sCurrentLine + "\n");
                }
                linenumber++;
            }

            if (oldvalue == -1) {
                summary = summary + "\n" + username + "," + rank + "," + mode ;
            }
        } catch (Exception e) {

            if (oldvalue == -1) {
                summary = username + "," + rank + "," + mode ;
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {

            }
        }
        BufferedWriter bw = null;

        try {
            File f=new File("summary.txt");
            if(f.exists()){
                f.delete();
                f.createNewFile();
            }else{
                f.createNewFile();
            }
            FileWriter fw = new FileWriter("summary.txt",true);
            bw = new BufferedWriter(fw);

            bw.write(summary);

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

}
