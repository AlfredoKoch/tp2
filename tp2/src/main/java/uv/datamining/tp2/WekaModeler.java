/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uv.datamining.tp2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private RandomForest modeloForest;

    public WekaModeler(MainFrame frame) {
        this.frame = frame;
    }

    //<>
    /**
     * Genera un archivo de formato ARFF a partir de un archivo .SCV Lo genera
     * en el mismo directorio con nombre archivo_csv.arff
     *
     * @param csv
     * @return
     * @throws Exception
     */
    public String csvToArff(File csv) throws Exception {
        if (!csv.getName().endsWith(".csv")) {
            throw new Exception("El archivo " + csv.getAbsolutePath() + " no es un archivo .csv");
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(csv);
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        String arff = csv.getAbsolutePath().substring(0, csv.getAbsolutePath().lastIndexOf(".")) + ".arff";
        saver.setFile(new File(arff));
//        saver.setDestination(new File(arff));
        saver.writeBatch();
        saver = null;
        return arff;
    }

    /**
     * Genera un arbol de clasificación de tipo J48 de Weka. Serializa el arbol
     * con nombre archivo_arff.modelJ48
     *
     * @param file
     * @param cm
     * @return nombre del archivo
     * @throws Exception
     */
    public String generarArbolJ48(File file, float cm) throws Exception {
        if(file==null){
            throw new Exception("Por favor cargue un archivo arff");
        }
        if(cm<0||cm>1){
            throw new Exception("Por favor seleccione un valor de CM mayor a 0 y menor a 1");
        }
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

        String fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".modelJ48";
        weka.core.SerializationHelper.write(fileName, modeloJ48);
        return fileName;

    }

    /**
     * Genera un modelo de clasificación tipo bayes ingenuo de Weka. Serializa
     * el modelo con nombre archivo_arff.modelBayes
     *
     * @param file
     * @return ruta del archivo
     * @throws Exception
     */
    public String generarBayes(File file) throws Exception {
        if(file==null){
            throw new Exception("Por favor cargue un archivo arff");
        }
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
        String fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".modelBayes";
        weka.core.SerializationHelper.write(fileName, modeloBayes);
        return fileName;

    }

    /**
     * Genera un modelo de clasificación tipo random forest de weka Serializa el
     * modelo con nombre archivo_arff.modelBayes
     *
     * @param file
     * @return nombre del archivo
     * @throws Exception
     */
    public String generarRamdomForest(File file) throws Exception {
        if(file==null){
            throw new Exception("Por favor cargue un archivo arff");
        }
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1); //columna con el atributo clase
        modeloForest = new RandomForest();

        modeloForest.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(modeloForest, data);
        frame.appendMessage("--- MODELO RandomForest ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN MODELO RandomForest ---");

        String fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".modelRForest";
        weka.core.SerializationHelper.write(fileName, modeloForest);
        return fileName;

    }

    public void readModelJ48(String absolutePath) throws Exception {
        modeloJ48 = (J48) weka.core.SerializationHelper.read(absolutePath);
    }

    public void readModelBayes(String absolutePath) throws Exception {
        modeloBayes = (NaiveBayes) weka.core.SerializationHelper.read(absolutePath);
    }

    public void readModelForest(String absolutePath) throws Exception {
        modeloForest = (RandomForest) weka.core.SerializationHelper.read(absolutePath);
    }

    public void testUsingModels(File file) throws Exception {
        if(modeloBayes==null || modeloJ48==null || modeloForest==null){
            throw new Exception("Debe cargar/crear los 3 modelos para poder probar");
        }
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(modeloJ48, data);
        frame.appendMessage("--- PRUEBAS MODELO J48 ---");
        frame.appendMessage(eval.toSummaryString());
        frame.appendMessage(eval.toMatrixString());
        frame.appendMessage("--- FIN PRUEBAS MODELO J48 ---");
        Evaluation eval2 = new Evaluation(data);
        eval2.evaluateModel(modeloBayes, data);
        frame.appendMessage("--- PRUEBAS MODELO Bayes ---");
        frame.appendMessage(eval2.toSummaryString());
        frame.appendMessage(eval2.toMatrixString());
        frame.appendMessage("--- FIN PRUEBAS MODELO Bayes ---");
        Evaluation eval3 = new Evaluation(data);
        eval3.evaluateModel(modeloForest, data);
        frame.appendMessage("--- PRUEBAS MODELO RandomForest ---");
        frame.appendMessage(eval3.toSummaryString());
        frame.appendMessage(eval3.toMatrixString());
        frame.appendMessage("--- FIN PRUEBAS MODELO RandomForest ---");
    }


    public void clasifyUnclasiffiedInstances(File file) throws Exception {
        if(modeloBayes==null || modeloJ48==null || modeloForest==null){
            throw new Exception("Debe cargar/crear los 3 modelos para poder clasificar");
        }
        Instances unlabeled = new Instances(new BufferedReader(new FileReader(file)));
        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        
        // create copy
        Instances labeledJ48 = new Instances(unlabeled);
        Instances labeledBayes = new Instances(unlabeled);
        Instances labeledRndForest = new Instances(unlabeled);
        Instances labeledDefinitive = new Instances(unlabeled);

        // label instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            
            double clasificationJ48 = modeloJ48.classifyInstance(labeledJ48.instance(i));
            double clasificationBayes = modeloBayes.classifyInstance(labeledBayes.instance(i));
            double clasificationRForest = modeloForest.classifyInstance(labeledRndForest.instance(i));
//            System.out.println("instance "+i+" "+clasificationJ48);
//            System.out.println(labeledJ48.classAttribute().value((int) clasificationJ48));
            labeledJ48.instance(i).setClassValue(clasificationJ48);
            labeledBayes.instance(i).setClassValue(clasificationBayes);
            labeledRndForest.instance(i).setClassValue(clasificationRForest);
            
            //todos lo clasificaron igual
            if(clasificationBayes==clasificationJ48 && clasificationBayes == clasificationRForest){ 
                labeledDefinitive.instance(i).setClassValue(clasificationBayes);
                continue;
            }
            //todos lo clasificaron distinto, prima random forest
            if(clasificationBayes!=clasificationJ48 && clasificationBayes!=clasificationRForest && clasificationJ48!=clasificationRForest){
                labeledDefinitive.instance(i).setClassValue(clasificationRForest);
                continue;
            }
            //bayes lo clasifico distinto, prima el random forest
            if(clasificationBayes!=clasificationJ48 && clasificationBayes!=clasificationRForest && clasificationJ48==clasificationRForest){
                labeledDefinitive.instance(i).setClassValue(clasificationRForest);
                continue;
            }
            //J48 lo clasifico distinto, prima el random forest
            if(clasificationJ48!=clasificationBayes && clasificationJ48!=clasificationRForest && clasificationBayes==clasificationRForest){
                labeledDefinitive.instance(i).setClassValue(clasificationRForest);
                continue;
            }
             //Forest lo clasifico distinto, prima el random J48
            if(clasificationRForest!=clasificationBayes && clasificationRForest!=clasificationJ48 && clasificationBayes==clasificationJ48){
                labeledDefinitive.instance(i).setClassValue(clasificationJ48);
            }   
            
        }
        
        // save labeled data
        String fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".clasifiedJ48";
        frame.appendMessage("Archivo clasificado por J48: "+fileName);
        frame.appendMessage("\n");
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(labeledJ48.toString());
        writer.newLine();
        writer.flush();
        writer.close();
        
        fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".clasifiedBayes";
        frame.appendMessage("Archivo clasificado por Bayes: "+fileName);
        frame.appendMessage("\n");
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(labeledBayes.toString());
        writer.newLine();
        writer.flush();
        writer.close();
        
        fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".clasifiedRFoest";
        frame.appendMessage("Archivo clasificado por Random Forest: "+fileName);
        frame.appendMessage("\n");
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(labeledRndForest.toString());
        writer.newLine();
        writer.flush();
        writer.close();
        
        fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".clasifiedDefinitive";
        frame.appendMessage("Archivo de clasificación unificada: "+fileName);
        writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(labeledDefinitive.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }

}
