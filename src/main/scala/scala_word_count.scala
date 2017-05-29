import org.apache.spark.SparkContext
import org.apache.hadoop.conf.Configuration
import org.bson.BSONObject

import scala.util.parsing.json._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.Row
import org.bson.BSONObject
import org.bson.BasicBSONObject
import org.elasticsearch.spark.sql._
import sun.util.resources.cldr.id.CurrencyNames_id

object scala_word_count {

  def main(args: Array[String])
  {
    val sc = new SparkContext("local", "Scala Word Count")
    val config = new Configuration()
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    //config.set("mongo.input.uri", "mongodb://127.0.0.1:27017/local.test_mongo_spark")
    config.set("mongo.input.uri", "mongodb://127.0.0.1:27017/local.new_plane_collection")
    config.set("mongo.output.uri", "mongodb://127.0.0.1:27017/local.output__")
    val mongoRDD = sc.newAPIHadoopRDD(config, classOf[com.mongodb.hadoop.MongoInputFormat], classOf[Object], classOf[BSONObject])

    //print("COUNT FROM MONGODB --- > " + mongoRDD.count())

    val bsonRDD = mongoRDD.map(x=>x._2.get("Plane_17_12_16"))	// Array[(Object, org.bson.BSONObject)] --> Array[org.bson.BSONObject]
    val jsonStringRDD = bsonRDD.map(x => x.toString)	// org.apache.spark.rdd.RDD[org.bson.BSONObject] --> org.apache.spark.rdd.RDD[String]
    //val jsonRDD = sqlContext.jsonRDD(jsonStringRDD)	// org.apache.spark.rdd.RDD[String] --> org.apache.spark.sql.DataFrame
    //val jsonRDD_mod = jsonRDD.withColumnRenamed("_id", "mongoDB_id")
    /*
    val saveRDD = jsonStringRDD.map(a=> {
        print(a)
    })
    */
    jsonStringRDD.foreach(a=>
    {
      val jparsed = JSON.parseFull(a)
      //val saveRDD = jparsed.map((tuple)
      print(jparsed)
    })


    /*
    val data__rdd = mongoRDD.foreach(a=>{
        val k = JSON.parseFull(a)
        print(k)
    })
    */
    /*
    val countsRDD = mongoRDD.flatMap(arg => {
      var str = arg._2.get("text").toString
      str = str.toLowerCase().replaceAll("[.,!?\n]", " ")
      str.split(" ")
    }).map(word => (word, 1)).reduceByKey((a, b) => a + b)

    val saveRDD = countsRDD.map((tuple) => {
      var bson = new BasicBSONObject()
      bson.put("word", tuple._1)
      bson.put("count", tuple._2)
      (null, bson)
    })
    saveRDD.saveAsNewAPIHadoopFile("E://spark-count.txt", classOf[Any], classOf[Any], classOf[com.mongodb.hadoop.MongoOutputFormat[Any, Any]], config)
    */
  }
}