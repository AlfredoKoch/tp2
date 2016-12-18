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
       
        m.runFull();
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
    
    public void runFull(){
        BufferedReader reader = null;
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            if (file == null) {
                System.out.println("error al cargar el archivo");
                return;
            }
            File FileOut = new File(file.getParent()+"/out_tp2_mejorado.csv");
            FileOutputStream fos = new FileOutputStream(FileOut);
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String[]> list = new ArrayList();
            String line = reader.readLine(); //fecha,tipo_propiedad,lugar,precio_COP,sup_total,sup_cubierta,piso,cuartos,titulo,clase
//            fecha	tipo_propiedad	lugar	precio_COP	sup_total	sup_cubierta	piso	cuartos	titulo	clase

//               0           1            2          3             4                     5        6        7      8     9
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
                if(lineCont[0].isEmpty() || lineCont[1].isEmpty() || lineCont[2].isEmpty() || lineCont[3].isEmpty()||lineCont[8].isEmpty()){
                    continue;
                }
                 if(lineCont[4].isEmpty() && lineCont[5].isEmpty()){
                        continue;
                    }
                 //cogemos solo los meses de la fecha
                 lineCont[0] = lineCont[0].split("/")[1];
                         
                //ahora miramos las bodegas
                if(lineCont[1].equals("store")){
                    //arreglamos el piso
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[7].isEmpty()){
                        lineCont[7]="1";
                    }
                    //miramos la superficie
                    if(lineCont[4].isEmpty() && !lineCont[5].isEmpty()){
                        lineCont[4] = lineCont[5];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[5].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[5] = lineCont[4];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                
                //los apartamentos
                if(lineCont[1].equals("apartment")){
                    //arreglamos el piso
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[7].isEmpty()){
                        lineCont[7]="2";
                    }
                    //miramos la superficie
                    if(lineCont[4].isEmpty() && !lineCont[5].isEmpty()){
                        lineCont[4] = lineCont[5];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[5].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[5] = lineCont[4];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                 //los apartamentos
                if(lineCont[1].equals("house")){
                    //arreglamos el piso
                    if(lineCont[6].isEmpty()){
                        lineCont[6]="1";
                    }
                    //arreglamos los cuartos
                    if(lineCont[7].isEmpty()){
                        lineCont[7]="3";
                    }
                    //miramos la superficie
                    if(lineCont[4].isEmpty() && !lineCont[5].isEmpty()){
                        lineCont[4] = lineCont[5];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                    if(lineCont[5].isEmpty() && !lineCont[4].isEmpty()){
                        lineCont[5] = lineCont[4];
//                         System.out.println("cambio de superficies en la linea "+k);
                    }
                }
                boolean tipo = false;
                if(lineCont[8].toLowerCase().contains("local")){
                    lineCont[8] = "local";
                    tipo=true;
                }
                if(lineCont[8].toLowerCase().contains("apartamento") || lineCont[8].toLowerCase().contains("apto")){
                    lineCont[8] = "apartamento";
                    tipo=true;
                }
                if(lineCont[8].toLowerCase().contains("oficina")){
                    lineCont[8] = "oficina";
                    tipo=true;
                }
                
                if(lineCont[8].toLowerCase().contains("casa")){
                    lineCont[8] = "casa";
                    tipo=true;
                }
                if(lineCont[8].toLowerCase().contains("finca")){
                    lineCont[8] = "finca";
                    tipo=true;
                }
                if(lineCont[8].toLowerCase().contains("bodega")){
                    tipo=true;
                    lineCont[8] = "bodega";
                }
                if(lineCont[8].toLowerCase().contains("lote")){
                    tipo=true;
                    lineCont[8] = "lote";
                }
                if(lineCont[8].toLowerCase().contains("penthouse")){
                    tipo=true;
                    lineCont[8] = "penthouse";
                }
                if(!tipo){
                    continue;
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
