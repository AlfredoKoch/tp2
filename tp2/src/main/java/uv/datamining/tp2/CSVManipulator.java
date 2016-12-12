/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uv.datamining.tp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author R4
 */
public class CSVManipulator {
    
    public static void main(String args[]){
        CSVManipulator m = new CSVManipulator();
       
        m.run();
    }
    //<>
    public void run(){
        BufferedReader reader = null;
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            if (file == null) {
                System.out.println("error al cargar el archivo");
                return;
            }
            File FileOut = new File(file.getParent()+"/out.csv");
            FileOutputStream fos = new FileOutputStream(FileOut);
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String[]> list = new ArrayList();
            String line = reader.readLine(); //tipo_propiedad,	lugar,	precio_COP,	sup_total,	sup_cubierta,	piso,	cuartos,	clase
//                                                    0           1         2               3               4           5       6               7
            fos.write(line.getBytes());
            fos.write("\n".getBytes());
            int k=1;
            while(line!=null){
                line = reader.readLine();
                if(line==null){
                    break;
                }
               k++;
                String[] lineCont = line.split(",");
                //si no tiene precio - remuevalo
                if(lineCont[0].isEmpty() || lineCont[1].isEmpty() || lineCont[2].isEmpty() ){
                    continue;
                }
                 if(lineCont[4].isEmpty() && lineCont[3].isEmpty()){
                        continue;
                    }
                //ahora miramos las bodegas
                if(lineCont[0].equals("store")){
                    //arreglamos el piso
                    if(lineCont[5].isEmpty()){
                        lineCont[5]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="1";
                    }
                    //miramos la superficie
                    if(lineCont[3].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[3] = lineCont[4];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[4].isEmpty() && !lineCont[3].isEmpty()){
                        lineCont[4] = lineCont[3];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                
                //los apartamentos
                if(lineCont[0].equals("apartment")){
                    //arreglamos el piso
                    if(lineCont[5].isEmpty()){
                        lineCont[5]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="2";
                    }
                    //miramos la superficie
                    if(lineCont[3].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[3] = lineCont[4];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[4].isEmpty() && !lineCont[3].isEmpty()){
                        lineCont[4] = lineCont[3];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                 //los apartamentos
                if(lineCont[0].equals("house")){
                    //arreglamos el piso
                    if(lineCont[5].isEmpty()){
                        lineCont[5]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="3";
                    }
                    //miramos la superficie
                    if(lineCont[3].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[3] = lineCont[4];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[4].isEmpty() && !lineCont[3].isEmpty()){
                        lineCont[4] = lineCont[3];
                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                for(int j=0; j<lineCont.length; j++){
                    fos.write(lineCont[j].getBytes());
                    if(j!=lineCont.length-1){
                        fos.write(",".getBytes());
                    }
                }
                fos.write("\n".getBytes());
                
            }
            fos.close();
            System.out.println("finished");
        } catch (Exception ex) {
            Logger.getLogger(CSVManipulator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(CSVManipulator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
