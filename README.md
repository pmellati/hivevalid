[![Build Status](https://travis-ci.org/pmellati/hivevalid.svg?branch=master)](https://travis-ci.org/pmellati/hivevalid)

# HiveValid

HiveValid is a tiny library for compiling and testing HiveQL queries. It is useful if you are generating hive queries programmatically, and want to test them.

The `HiveSupport` file is stolen from the [thermometer](https://github.com/CommBank/thermometer) project, so most of the credit for this project goes to thermometer's devs.

HiveValid basically allows you to run a fake hive environment. In this environment, you are allowed to:
* Manipulate hive metadata using the `HiveValid.runMeta` function. This allows you to create / define databases and tables in the environment.
* Compile (but not run) a query using the `HiveValid.compile` function, to check whether a query makes sense.

Additionally, there is a [specs2](http://etorreborre.github.io/specs2/) matcher for checking if two hive queries are the same (and that both compile). For example:

```scala
import hivevalid.HiveValid
import hivevalid.Matchers._
import org.specs2.mutable.Specification
import me.Test.myGeneratedQuery

class HqlGenSpec extends Specification {
  "myGeneratedQuery" should {
    "be what I want it to be" in {
      HiveValid.runMeta("""CREATE DATABASE IF NOT EXISTS db""")

      HiveValid.runMeta(s"""
        |CREATE TABLE IF NOT EXISTS cars ( car_id String, model String )
        |ROW FORMAT DELIMITED
        |FIELDS TERMINATED BY '\t'
        |LINES TERMINATED BY '\n'
        |STORED AS TEXTFILE
      """.stripMargin)

      // Will check that both `myGeneratedQuery` and the expected query compile
      // and that they are the same.
      myGeneratedQuery must beSameHqlAs("""
        |SELECT A.model
        |FROM   db.cars A
      """.stripMargin)
    }
  }
}
```

## Getting started

In order to add HiveValid to your sbt project as a dependency, first add the following resolvers:

```scala
resolvers ++= Seq(
  Resolver.bintrayIvyRepo("pmellati", "maven"),
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "cloudera"       at "https://repository.cloudera.com/content/repositories/releases/",
  "spring-plugins" at "http://repo.spring.io/plugins-release/"
)
```

Next, add the following dependencies (find the appropriate versions for bellow by looking at this project's [build.sbt](https://github.com/pmellati/hivevalid/blob/master/build.sbt)):

```scala
libraryDependencies ++= Seq(
  "hivevalid"  %% "hivevalid" % <hivevalid version> % "test",

  // The following two are optional. Only add them if you are planning to use
  // HiveValid's specs2 matcher(s).
  "org.specs2" %% "specs2-core"          % <specs2 version> % "test",
  "org.specs2" %% "specs2-matcher-extra" % <specs2 version> % "test"
)
```

And you're all set.
