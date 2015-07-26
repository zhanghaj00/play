name := "play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws ,"net.debasishg" %% "redisclient" % "[2.15,)",)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  