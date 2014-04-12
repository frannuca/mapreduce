package org.fjn.mapreduce.interface


import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

class MapReduceFunctionBaseTest extends AssertionsForJUnit {


  val testClass = new MapReduceFunctionBase[Int,Double,String] {



    override def getDefaultAccumulator:String={
      "0"
    }
    override def getID:String={
      "Test"
    }
    override  def reduceExecutor(acc:String,x:Double):String={
      acc + "+" + x.toString
    }

    override def mapExecutor(x:Int):Double={
      x.toDouble
    }

  }


  @Test def TestSimpleMapReduce{

    val ok = testClass.mapReduce(Seq(1,2))

    assert( ok === "0+1.0+2.0")

  }
}
