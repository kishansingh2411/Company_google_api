import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions.udf

object StandardisationGoogleAPI {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("GoogleAPI-Standardisation").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)


    val input_path = args(0)
    val output_path = args(1)
    val isEducation: Boolean = args(2).toBoolean


    import sqlContext.implicits._
    val inputData = sc.textFile(input_path).toDF("Name")

   //inputData.show()

    val standardisation = udf {
    var output = ""
    var str=""
    (name: String ) => {
      str = "\"".concat(name).concat("\"")
      output = GoogleAPICall.call(str,isEducation)
    }
      output
  }
    println("input: "+input_path)
    println("output : "+ output_path)
      val newfinalDF = inputData.withColumn("Standard",standardisation(inputData("Name")))
      //newfinalDF.show(110,false)
    newfinalDF.coalesce(1).write.format("com.databricks.spark.csv").option("header", "false").option("delimiter",";").save(output_path+"/output")

  }
}
