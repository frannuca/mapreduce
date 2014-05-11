package org.fjn.mapreduce.server

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Test

/**
 * Created by fran on 11.05.2014.
 */
class MapreduceService extends AssertionsForJUnit{

 @Test def runServer{

    val server = new CallMapReduceWithJARService

    server.Start

    import scala.concurrent.duration._
    scala.concurrent.Await.result(server.consumerThread.get,Duration.Inf)
    server.Stop

  }

}
