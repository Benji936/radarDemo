package demo.rad.ar.main;

import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;

import java.io.IOException;
import java.util.List;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;

import demo.rad.ar.main.models.UserSession;
import demo.rad.ar.main.services.SessionService;

public class AIModel {
    @Autowired
    private SessionService sessionService;

    public void trainSparkModel() throws IOException {
        Dataset<Row> sessionDF = sessionService.loadUserSessionData();
    
        // Convert categorical columns to numerical using StringIndexer
        StringIndexer deviceTypeIndexer = new StringIndexer()
            .setInputCol("deviceType")
            .setOutputCol("deviceTypeIndex");
    
        StringIndexer countryIndexer = new StringIndexer()
            .setInputCol("country")
            .setOutputCol("countryIndex");
    
        // Feature Vector Assembler (Combines all features into one column)
        VectorAssembler assembler = new VectorAssembler()
            .setInputCols(new String[]{"deviceTypeIndex", "countryIndex", "temperature", "humidity", "screenWidth", "screenHeight"})
            .setOutputCol("features");
    
        // Define Random Forest Classifier for productId prediction
        RandomForestClassifier productClassifier = new RandomForestClassifier()
            .setLabelCol("productId")
            .setFeaturesCol("features");
    
        // Build Pipeline (Data Transformation + Model Training)
        Pipeline pipeline = new Pipeline()
            .setStages(new org.apache.spark.ml.PipelineStage[]{deviceTypeIndexer, countryIndexer, assembler, productClassifier});
    
        // Train Model
        PipelineModel model = pipeline.fit(sessionDF);
    
        // Save Model
        model.write().overwrite().save("spark-product-model");
    
        System.out.println("Spark ML model trained and saved.");
    }


    public String predictProduct(UserSession session) {
        try {
            SparkSession spark = sessionService.getSparkSession();

            // Load trained Spark ML model
            PipelineModel model = PipelineModel.load("spark-product-model");

            // Convert session to DataFrame
            List<UserSession> singleSession = List.of(session);
            Dataset<Row> sessionDF = spark.createDataFrame(singleSession, UserSession.class);

            // Make prediction
            Dataset<Row> predictions = model.transform(sessionDF);
            predictions.show(); // Debugging: See predictions

            // Extract predicted productId
            Row predictionRow = predictions.select("prediction").first();
            return String.valueOf(predictionRow.getDouble(0));

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
