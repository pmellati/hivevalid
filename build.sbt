lazy val Version = new {
  val scala         = "2.11.8"
  val scalaz        = "7.2.6"
  val hadoop        = "2.5.0-mr1-cdh5.3.10"
  val hadoopNoMr1   = "2.5.0-cdh5.3.8"
  val parquet       = "1.5.0-cdh5.3.8"
  val parquetFormat = "2.1.0-cdh5.3.8"
  val slf4j         = "1.7.5"
  val avro          = "1.7.6-cdh5.3.8"
  val zookeeper     = "3.4.5-cdh5.3.8"
  val guava         = "11.0.2"
  val jackson       = "1.8.8"
  val jetty         = "6.1.26.cloudera.4"
  val asm           = "3.2"
}

lazy val hivevalid = project
  .in(file("."))
  .settings(
    version := "0.1.5",
    publishMavenStyle := false,

    scalaVersion := Version.scala,

    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
      "cloudera"       at "https://repository.cloudera.com/content/repositories/releases/",
      "spring-plugins" at "http://repo.spring.io/plugins-release/"
    ),

    conflictManager := ConflictManager.strict,

    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-feature",
      "-deprecation",
      "-language:_"
    ),

    scalacOptions in Test ++= Seq("-Yrangepos"),

    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),

    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-core" % Version.scalaz,
      noHadoop("org.apache.hadoop" % "hadoop-client" % Version.hadoop),
      noHadoop("org.apache.hive"   % "hive-exec"     % "0.13.1-cdh5.3.8"),
      noHadoop("org.apache.hive"   % "hive-service"  % "0.13.1-cdh5.3.8")   // TODO: can we remove this?
    ) ++ hadoopClasspath
  )

def noHadoop(module: ModuleID) = module.copy(
  exclusions = module.exclusions ++ hadoopCP.exclusions
)

def hadoopClasspath = hadoopCP.modules.map(_.intransitive)

lazy val hadoopCP = new {
  val modules = List[ModuleID](
    "org.apache.hadoop"            % "hadoop-core"               % Version.hadoop,
    "org.apache.hadoop"            % "hadoop-tools"              % Version.hadoop,
    "org.apache.hadoop"            % "hadoop-annotations"        % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-auth"               % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-common"             % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-hdfs"               % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-hdfs-nfs"           % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-nfs"                % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-yarn-api"           % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-yarn-client"        % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-yarn-common"        % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-yarn-server-common" % Version.hadoopNoMr1,
    "com.twitter"                  % "parquet-avro"              % Version.parquet,
    "com.twitter"                  % "parquet-column"            % Version.parquet,
    "com.twitter"                  % "parquet-common"            % Version.parquet,
    "com.twitter"                  % "parquet-encoding"          % Version.parquet,
    "com.twitter"                  % "parquet-generator"         % Version.parquet,
    "com.twitter"                  % "parquet-hadoop"            % Version.parquet,
    "com.twitter"                  % "parquet-jackson"           % Version.parquet,
    "com.twitter"                  % "parquet-format"            % Version.parquetFormat,
    "org.slf4j"                    % "slf4j-api"                 % Version.slf4j,
    "org.slf4j"                    % "slf4j-log4j12"             % Version.slf4j,
    "commons-beanutils"            % "commons-beanutils"         % "1.7.0",
    "commons-beanutils"            % "commons-beanutils-core"    % "1.8.0",
    "commons-cli"                  % "commons-cli"               % "1.2",
    "commons-codec"                % "commons-codec"             % "1.4",
    "commons-collections"          % "commons-collections"       % "3.2.1",
    "org.apache.commons"           % "commons-compress"          % "1.4.1",
    "commons-configuration"        % "commons-configuration"     % "1.6",
    "commons-daemon"               % "commons-daemon"            % "1.0.13",
    "commons-digester"             % "commons-digester"          % "1.8",
    "commons-el"                   % "commons-el"                % "1.0",
    "commons-httpclient"           % "commons-httpclient"        % "3.1",
    "commons-io"                   % "commons-io"                % "2.4",
    "commons-lang"                 % "commons-lang"              % "2.6",
    "commons-logging"              % "commons-logging"           % "1.1.3",
    "commons-net"                  % "commons-net"               % "3.1",
    "org.apache.commons"           % "commons-math3"             % "3.1.1",
    "org.apache.httpcomponents"    % "httpclient"                % "4.2.5",
    "org.apache.httpcomponents"    % "httpcore"                  % "4.2.5",
    "org.apache.avro"              % "avro"                      % Version.avro,
    "org.apache.zookeeper"         % "zookeeper"                 % Version.zookeeper,
    "com.google.code.findbugs"     % "jsr305"                    % "1.3.9",
    "com.google.guava"             % "guava"                     % Version.guava,
    "com.google.protobuf"          % "protobuf-java"             % "2.5.0",
    "com.google.inject"            % "guice"                     % "3.0",
    "com.google.inject.extensions" % "guice-servlet"             % "3.0",
    "org.codehaus.jackson"         % "jackson-mapper-asl"        % Version.jackson,
    "org.codehaus.jackson"         % "jackson-core-asl"          % Version.jackson,
    "org.codehaus.jackson"         % "jackson-jaxrs"             % Version.jackson,
    "org.codehaus.jackson"         % "jackson-xc"                % Version.jackson,
    "org.codehaus.jettison"        % "jettison"                  % "1.1",
    "org.xerial.snappy"            % "snappy-java"               % "1.0.4.1",
    "junit"                        % "junit"                     % "4.11",
    "jline"                        % "jline"                     % "0.9.94",
    "org.mortbay.jetty"            % "jetty"                     % Version.jetty,
    "org.mortbay.jetty"            % "jetty-util"                % Version.jetty,
    "hsqldb"                       % "hsqldb"                    % "1.8.0.10",
    "ant-contrib"                  % "ant-contrib"               % "1.0b3",
    "aopalliance"                  % "aopalliance"               % "1.0",
    "javax.inject"                 % "javax.inject"              % "1",
    "javax.xml.bind"               % "jaxb-api"                  % "2.2.2",
    "com.sun.xml.bind"             % "jaxb-impl"                 % "2.2.3-1",
    "javax.servlet"                % "servlet-api"               % "2.5",
    "javax.xml.stream"             % "stax-api"                  % "1.0-2",
    "javax.activation"             % "activation"                % "1.1",
    "com.sun.jersey"               % "jersey-client"             % "1.9",
    "com.sun.jersey"               % "jersey-core"               % "1.9",
    "com.sun.jersey"               % "jersey-server"             % "1.9",
    "com.sun.jersey"               % "jersey-json"               % "1.9",
    "com.sun.jersey.contribs"      % "jersey-guice"              % "1.9",
    "org.fusesource.leveldbjni"    % "leveldbjni-all"            % "1.8",
    "asm"                          % "asm"                       % Version.asm,
    "io.netty"                     % "netty"                     % "3.6.2.Final"
  )

  // Different versions of these jars have different organizations. Could do
  // something complicated to change old version to new, but for now just
  // keep a list of alternate versions so can exclude both versions
  val alternateVersions = List[ModuleID](
    "org.ow2.asm"                  % "asm"                       % "4.1",
    "org.jboss.netty"              % "netty"                     % "3.2.2.Final",
    "stax"                         % "stax-api"                  % "1.0.1"
  )

  // These jars have classes which interfere with the classes provided by hadoop
  // so we exclude these as well
  val interferingModules = List[ModuleID](
    "org.apache.hadoop"            % "hadoop-mapreduce-client-core"   % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-mapreduce-client-common" % Version.hadoopNoMr1,
    "org.apache.hadoop"            % "hadoop-client"                  % Version.hadoopNoMr1
  )

  val exclusions =
    (modules ++ alternateVersions ++ interferingModules)
      .map(m => ExclusionRule(m.organization, m.name))
}