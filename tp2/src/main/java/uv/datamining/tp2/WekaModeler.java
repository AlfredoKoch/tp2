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
import weka.classifiers.trees.RandomForest;

/**
 *
 * @author R4
 */
public class WekaModeler {
    
    private MainFrame frame;
    private J48 modeloJ48;
    private NaiveBayes modeloBayes;
    private RandomForest forest;
    
    
    
    
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
     * Serializa el arbol con nombre archivo_arff.modelJ48
     * 
     * @param file
     * @param cm
     * @return nombre del archivo
     * @throws Exception 
     */
    public String generarArbolJ48(File file, float cm) throws Exception{
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
        
        String fileName = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelJ48";
        weka.core.SerializationHelper.write(fileName,modeloJ48);
        return fileName;
        
    }
    
    /**
     * Genera un modelo de clasificación tipo bayes ingenuo de Weka.
     * Serializa el modelo con nombre archivo_arff.modelBayes
     * 
     * @param file
     * @param cm
     * @return ruta del archivo
     * @throws Exception 
     */
    public String generarBayes(File file) throws Exception{
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        modeloBayes = new NaiveBayes();
        modeloBayes.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(modeloBayes, data);
        frame.appendMessage("--- MODELO NaiveBayes ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN MODELO NaiveBayes ---");
        String fileName = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelBayes";
        weka.core.SerializationHelper.write(fileName,modeloJ48);
        return fileName;
        
    }
    
    /**
     * Genera un modelo de clasificación tipo random forest de weka
     * Serializa el modelo con nombre archivo_arff.modelBayes
     * 
     * @param file
     * @return nombre del archivo
     * @throws Exception 
     */
    public String generarRamdomForest(File file) throws Exception{
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        forest = new RandomForest();
        
        forest.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(forest, data);
        frame.appendMessage("--- MODELO RandomForest ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN MODELO RandomForest ---");
        
        String fileName = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelRForest";
        weka.core.SerializationHelper.write(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(".")) +".modelRForest",modeloJ48);
        return fileName;
        
    }

    public void readModelJ48(String absolutePath) throws Exception {
        modeloJ48 = (J48) weka.core.SerializationHelper.read(absolutePath);
    }
    
    public void readModelBayes(String absolutePath) throws Exception {
        modeloBayes = (NaiveBayes) weka.core.SerializationHelper.read(absolutePath);
    }
    
    public void readModelForest(String absolutePath) throws Exception {
        forest = (RandomForest) weka.core.SerializationHelper.read(absolutePath);
    }
    
}
