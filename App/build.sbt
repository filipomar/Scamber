name := "Escamber"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	javaJdbc,
	javaEbean,
	cache,
	filters,
	"mysql" % "mysql-connector-java" % "5.1.27",
	"commons-io" % "commons-io" % "2.2"
)     

play.Project.playJavaSettings
