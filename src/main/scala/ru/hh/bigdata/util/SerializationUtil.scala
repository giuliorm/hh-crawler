package ru.hh.bigdata

import com.mongodb.{BasicDBList, BasicDBObject}
import org.apache.spark.rdd.RDD
import org.bson.BasicBSONObject

/**
  * Created by JuriaSan on 03.06.2017.
  */
object SerializationUtil {

  def serialize(tuple: Tuple2[String, Int]): BasicBSONObject = {
    var o = new BasicBSONObject()
    o.put("word", tuple._1)
    o.put("count", new java.lang.Integer(tuple._2))
    o
  }

  def serialize(list: List[Tuple2[String, Int]]): BasicDBList = {
     var l = new BasicDBList()
     list.foreach(w => {
      val o = SerializationUtil.serialize(w)
      l.add(o)
    })
    l
  }

  def serialize(vacancies: RDD[List[Tuple2[String, Int]]]):
   RDD[(Null, BasicBSONObject)] = {
    vacancies.map(wc => {
      var bson = new BasicBSONObject()
      var words = SerializationUtil.serialize(wc)
      bson.put("words", words)
      (null, bson)
    })
  }
}
