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
import weka.classifiers.bayes.NaiveBayes;

/**
 *
 * @author R4
 */
public class WekaModeler {
    
    private MainFrame frame;
    private J48 modeloJ48;
    private NaiveBayes modeloBayes;
    
    
    
    
    public WekaModeler(MainFrame frame){
        this.frame = frame;
    }
    
    //<>
    /**
     * Genera un archivo de formato ARFF a partir de un archivo .SCV
     * Lo genera en el mismo directorio con nombre archivo_csv.arff
     * 
     * @param csv
     * @return
     * @throws Exception 
     */
    public String csvToArff(File csv) throws Exception{
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
        saver.setFile(new File(arff));
//        saver.setDestination(new File(arff));
        saver.writeBatch();
        saver = null;
        return arff;
    }
    
    /**
     * Genera un arbol de clasificación de tipo J48 de Weka.
     * Serializa el arbol con nombre archivo_arff.model
     * 
     * @param file
     * @param cm
     * @throws Exception 
     */
    public void generarArbolJ48(File file, float cm) throws Exception{
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        modeloJ48 = new J48();
        modeloJ48.setConfidenceFactor(cm);
        modeloJ48.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(modeloJ48, data);
        frame.appendMessage("--- MODELO J48 ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN MODELO J48 ---");
        
        
        weka.core.SerializationHelper.write(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelJ48",modeloJ48);
        
    }
    
    /**
     * Genera un arbol de clasificación de tipo J48 de Weka.
     * Serializa el arbol con nombre archivo_arff.model
     * 
     * @param file
     * @param cm
     * @throws Exception 
     */
    public void generarBayes(File file) throws Exception{
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        modeloBayes = new NaiveBayes();
        modeloBayes.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(modeloBayes, data);
        frame.appendMessage("--- MODELO J48 ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN MODELO J48 ---");
        
        weka.core.SerializationHelper.write(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelBayes",modeloJ48);
        
    }

    void readModelJ48(String absolutePath) throws Exception {
        modeloJ48 = (J48) weka.core.SerializationHelper.read(absolutePath);
    }
    
    void readModelBayes(String absolutePath) throws Exception {
        modeloBayes = (NaiveBayes) weka.core.SerializationHelper.read(absolutePath);
    }
    
}
