package org.fjn.mapreduce.interface


import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.reflect.runtime.{universe => ru, _}
import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.reflect.runtime.{universe => ru}

/**
 * Base interface for all the
 */
abstract class IMapReduceFunction{
  /**
   * used to get the uuid of this specific function. This uuid is useful to ask server if the given function is already
   * loaded in the server and therefor no need to re-transfer it.
   * @return
   */
  def getID:String
  def run(x:Seq[Any]):Any
}


/**
 * This class implements a container to execute map reduction
 * @tparam A         Type of the items in the Seq to be reduces
 * @tparam mapR      Type of transformed sequence
 * @tparam reduceR   final type after applying reduce function
 */
abstract class MapReduceFunctionBase[A:TypeTag,mapR:TypeTag,reduceR:TypeTag] extends IMapReduceFunction{


  protected def getDefaultAccumulator:reduceR
  ///Basic identifier to be used to detect this function on server side
  def getID:String

  /**
   * execution method to process elements in the map phase. 
   * @param x item of type A from the given sequence on which to apply the transformation to type mapR
   * @return transformed map item of type mapR
   */
  protected def mapExecutor(x:A):mapR

  /**
   *method to be applied to the fold/reduce action after the map has been applied 
   * @param x  input parameter resulting from the map
   * @param acc 
   * @return
   */
  protected def reduceExecutor(acc:reduceR,x:mapR):reduceR


  /**
   * executes the map and reduce on the provided sequence
   * @param x input sequence of elements of type A
   * @return final aggregated value of type reduceR
   */
  def mapReduce(x:Seq[A]):reduceR= x.par.map(mapExecutor).foldLeft(getDefaultAccumulator)(reduceExecutor)

  def run(x:Seq[Any]):Any={
    mapReduce(x.map(_.asInstanceOf[A]))
  }

}