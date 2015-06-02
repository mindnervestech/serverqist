name := "serverqist"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaCore,
  "javax.mail" % "mail" % "1.4",
  "mysql" % "mysql-connector-java" % "5.1.18"
)     

play.Project.playJavaSettings
