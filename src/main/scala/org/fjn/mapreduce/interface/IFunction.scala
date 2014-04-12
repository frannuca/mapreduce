package org.fjn.mapreduce.interface


import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.reflect.runtime.{universe => ru, _}


/**
 * Base interface for all the
 */
trait IFunction{
  /**
   * used to get the uuid of this specific function. This uuid is useful to ask server if the given function is already
   * loaded in the server and therefor no need to re-transfer it.
   * @return
   */
  def getID:String
}
