import sbt._
import sbt.Keys._

//import twirl.sbt.TwirlPlugin._

object BuildSettings {


  val buildOrganization = "org.fjn"
  val buildVersion = "1.0.0"
  val buildScalaVersion = "2.10.3"
  val scalazVersion = "7.0.5"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion //,
  )
}

object Resolvers {

  val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val sonatype1 = "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
  val sonatype2 = "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
}

object Dependencies {


  val actors = "com.typesafe.akka" %% "akka-actor" % "2.2.3" 
  val apacheMath = "org.apache.commons" % "commons-math3" % "3.0"    
  val akkaConf = "com.typesafe" % "config" % "1.2.0"
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % "2.2.3"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.2.3"
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % "2.2.3"
  val junit = "junit" % "junit" % "4.11"
  val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
  val scalaReflect = "org.scala-lang" % "scala-reflect" % BuildSettings.buildScalaVersion
  val log4j = "log4j" % "log4j" % "1.2.14"
}

object ScalaMatBuild extends Build {

  import Resolvers._
  import Dependencies._
  import BuildSettings._

  /**
   * top layer
   */
  lazy val scalaMatPrj = Project(
    "mapreduce",
    file("."),
    settings = buildSettings ++ Seq(resolvers := Seq(typesafe, sonatype1, sonatype2), libraryDependencies ++=
      Seq(actors, apacheMath, scalaTest,akkaConf,akkaRemote,akkaActor,akkaCluster, junit, scalaTest, scalaReflect,log4j))
  ) 


}
