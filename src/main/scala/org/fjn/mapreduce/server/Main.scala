package org.fjn.mapreduce.server

import org.apache.log4j.Logger

/**
 * Created by fran on 11.05.2014.
 */
object MainServerApp extends App{
  val logger = Logger.getLogger("MainServerApp");

  logger.debug("initializing server")

  val server = new CallMapReduceWithJARService
  logger.debug("starting the server")

  server.Start
  logger.debug("server started")

  import scala.concurrent.duration._
  scala.concurrent.Await.result(server.consumerThread.get,Duration.Inf)
  server.Stop


}
