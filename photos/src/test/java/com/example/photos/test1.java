package com.example.photos;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
//import org.nd4j.linalg.io.ClassPathResource;
//import org.nd4j.linalg.io.Resource;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class test1 {
    public static void main(String[] args) throws Exception {
        System.out.println("111");
    }
    }