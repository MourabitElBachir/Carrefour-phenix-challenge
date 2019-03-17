name := "Carrefour"

version := "1.2.8"

scalaVersion := "2.12.8"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "com.concurrentthought.cla" %% "command-line-arguments" % "0.5.0",
  "com.concurrentthought.cla" %% "command-line-arguments-examples" % "0.5.0"
)

coverageExcludedPackages := ".*Main.scala;.*MainComputation.scala"