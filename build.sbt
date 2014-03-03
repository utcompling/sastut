name := "sastut"

version := "0.1"

organization := "com.jasonbaldridge"

scalaVersion := "2.10.3"

retrieveManaged := true

crossPaths := false

resolvers ++= Seq(
  "opennlp sourceforge repo" at "http://opennlp.sourceforge.net/maven2",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "org.apache.opennlp" % "opennlp-tools" % "1.5.3",
  "org.scalanlp" % "nak" % "1.2.1-SNAPSHOT",
  "org.scalanlp" % "chalk" % "1.1.3-SNAPSHOT",
  "org.rogach" %% "scallop" % "0.9.5",
  "gov.nist.math" % "jama" % "1.0.2" 
)
