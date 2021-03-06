import sbtrelease._
import ReleasePlugin._
import ReleaseStateTransformations._

sbtPlugin := true

name := "sbt-docker-compose"

organization := "com.tapad"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq("net.liftweb" %% "lift-json" % "2.5-RC5",
  "org.yaml" % "snakeyaml" % "1.15",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test")

publishTo := {
  val nexus = "https://oss.sonatype.org"
  if (isSnapshot.value)
    Some("snapshots" at s"$nexus/content/repositories/snapshots")
  else
    Some("releases" at s"$nexus/service/local/staging/deploy/maven2")
}

useGpg := true

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := {
  <url>http://github.com/Tapad/sbt-docker-compose</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://opensource.org/licenses/BSD-3-Clause</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:Tapad/sbt-docker-compose.git</url>
      <connection>scm:git:git@github.com:Tapad/sbt-docker-compose.git</connection>
    </scm>
    <developers>
      <developer>
        <id>kurt.kopchik@tapad.com</id>
        <name>Kurt Kopchik</name>
        <url>http://github.com/kurtkopchik</url>
      </developer>
    </developers>
  }

scalariformSettings

releaseSettings

ReleaseKeys.nextVersion := { (version: String) => Version(version).map(_.bumpBugfix.asSnapshot.string).getOrElse(versionFormatError) }

ReleaseKeys.releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
