name := "serverqist"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaCore,
  "javax.mail" % "mail" % "1.4",
  "commons-codec" % "commons-codec" % "1.10",
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.ganyo" % "gcm-server" % "1.0.2",
  "com.googlecode.json-simple" % "json-simple" % "1.1"
)     

play.Project.playJavaSettings
