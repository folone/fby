resolvers := Seq(Resolver.sonatypeRepo("public"))

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-Xexperimental")

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.5.10",
  "com.chuusai" %% "shapeless" % "2.3.2"
)
