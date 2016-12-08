/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uv.datamining.tp2;

import java.io.File;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.classifiers.trees.J48;
import weka.core.converters.ArffLoader;

/**
 *
 * @author R4
 */
public class WekaModeler {
    
    public static String csvToArff(File csv) throws Exception{
        if(!csv.getName().endsWith(".csv")){
            throw new Exception("El archivo "+csv.getAbsolutePath()+" no es un archivo .csv");
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(csv);
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        String arff = csv.getAbsolutePath().substring(0,csv.getAbsolutePath().lastIndexOf(".")) +".arff";
        System.out.println(arff);
        saver.setFile(new File(arff));
//        saver.setDestination(new File(arff));
        saver.writeBatch();
        saver = null;
        return arff;
    }
    
    public static void generarArbol(File file, float cm) throws Exception{
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        J48 tree = new J48();
        tree.setConfidenceFactor(cm);
        tree.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(tree, data);
        System.out.println(eval.toSummaryString());
        
        weka.core.SerializationHelper.write(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".model",tree);
        
    }
    
}
