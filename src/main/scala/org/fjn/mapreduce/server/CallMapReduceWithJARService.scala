package org.fjn.mapreduce.server

import org.fjn.rabbit.server.RMQRPCServer
import org.fjn.mapreduce.messages.MapReduceMessages.{CallMapReduceWithJARResponse, CallMapReduceWithJARRequest}
import com.typesafe.config.ConfigFactory
import java.io.{File, FileOutputStream, BufferedOutputStream}
import org.fjn.mapreduce.interface.IMapReduceFunction
import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration

object CallMapReduceWithJARService{

  private val conf = ConfigFactory.load()
  val libFolder = conf.getString("fileSytem.libraryPath")


  def storeLibrary(base64:String):(String,String)={
    import sun.misc.BASE64Decoder

    val decoder = new BASE64Decoder()
    val decodedBytes = decoder.decodeBuffer(base64)



    val libraryId = java.util.UUID.randomUUID().toString
    val filePath = s"${libFolder}${libraryId}.jar"
    val ostream = new BufferedOutputStream(new FileOutputStream(filePath))
    ostream.write(decodedBytes,0,decodedBytes.length)
    (libraryId,filePath)

  }
  def apply(msg:CallMapReduceWithJARRequest):CallMapReduceWithJARResponse={

    val jar = msg.getLibrary
    val clazzPath =jar.getClazzPath
    val (libraryId,filePath) = storeLibrary( jar.getJarPayload)


    var classLoader = new java.net.URLClassLoader(
      Array(new File(filePath).toURI.toURL),
      /*
       * need to specify parent, so we have all class instances
       * in current context
       */
      this.getClass.getClassLoader)

      val clazz = classLoader.loadClass(clazzPath)



    val instance = clazz.newInstance();

    if(instance.isInstanceOf[IMapReduceFunction]){
      val module = instance.asInstanceOf[IMapReduceFunction]

      val result = module.run(msg.getSeriesList.asScala).asInstanceOf[Double]

      val response = CallMapReduceWithJARResponse.newBuilder().setExecutionTimeMsMap(0).setExecutionTimeMsReduce(0)
      .setValue(result).build()

      response

  }
    else{
      throw new Throwable("sent jar does not contain classes derived from IMapReduceFunction")
    }
  }


  val server = new CallMapReduceWithJARService
  server.Start
  scala.concurrent.Await.result(server.consumerThread.get,Duration.Inf)

}


class CallMapReduceWithJARService extends RMQRPCServer[CallMapReduceWithJARRequest,CallMapReduceWithJARResponse](CallMapReduceWithJARService.apply){



}
