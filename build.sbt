name := "serverqist"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaCore,
  "javax.mail" % "mail" % "1.4",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.0",
  "commons-codec" % "commons-codec" % "1.10",
  "mysql" % "mysql-connector-java" % "5.1.18"
)     

play.Project.playJavaSettings
