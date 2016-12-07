package hivevalid

import org.apache.hadoop.hive.ql.Driver
import org.apache.hadoop.hive.ql.processors.CommandProcessorResponse
import org.apache.hadoop.hive.ql.session.SessionState
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object HiveValid {
  lazy val hiveConf =
    Await.result(HiveSupport.initialize(log = logger.info), Duration.Inf)

  def runMeta(query: String): CommandProcessorResponse = withDriver {
    _ run query
  }

  def compile(query: String): CompileResult = withDriver { driver =>
    if (driver.compile(query) == 0)
      CompileResult.Success
    else
      CompileResult.Failure(driver.getErrorMsg)
  }

  sealed trait CompileResult
  object CompileResult {
    case object Success              extends CompileResult
    case class  Failure(msg: String) extends CompileResult
  }

  private def withDriver[R](action: Driver => R): R = {
    SessionState.start(hiveConf)
    SessionState.get.setIsSilent(true)

    val driver = new Driver(hiveConf)

    try {
      driver.init()
      action(driver)
    } finally {
      driver.close()
      SessionState.detachSession()
    }
  }

  private val logger = LoggerFactory.getLogger(HiveValid.getClass)
}