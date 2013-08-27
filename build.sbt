import AssemblyKeys._

name := "students_db"

version := "0.2-SNAPSHOT"

organization := "dbproject"

scalaVersion := "2.10.2"

scalariformSettings

org.scalastyle.sbt.ScalastylePlugin.Settings

assemblySettings

jarName in assembly <<= (name, version) map { (name, version) => name + "-" + version + ".jar" }

/* Shell */
shellPrompt := { state => "students_db" + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  Seq(
    "ch.qos.logback"    	%     	"logback-classic"       %   "1.0.9",
    "com.typesafe"      	%%    	"scalalogging-slf4j"    %   "1.0.1",
    "com.typesafe"      	%     	"config"                %   "1.0.0",
    "com.typesafe.slick" 	%% 		"slick" 				% 	"1.0.1-RC1",
    "com.h2database"    	%     	"h2"                    %   "1.3.167",
    "org.xerial" 			% 		"sqlite-jdbc" 			% 	"3.7.2"
  )
}