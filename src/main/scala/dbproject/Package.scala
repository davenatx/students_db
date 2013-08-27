import com.typesafe.config.ConfigFactory
import scala.slick.driver.H2Driver.simple._
import com.typesafe.scalalogging.slf4j.Logging
import dbproject.dao._

/**
 * Package Object containing global information
 */
package object dbproject extends Logging {

  // Load properties
  private val config = ConfigFactory.load("settings.properties")

  lazy val dbDriver = config.getString("dbDriver")
  lazy val dbUrl = config.getString("dbUrl")
  lazy val parentsTableName = config.getString("parentsTableName")
  lazy val studentsTableName = config.getString("studentsTableName")
  lazy val exportFileName = config.getString("exportFileName")

  // Package level access to the database
  lazy val db = Database.forURL(dbUrl, driver = dbDriver)

  // Package level access to the data access objects
  lazy val parentDao = new ParentDAO
  lazy val studentDao = new StudentDAO

  /**
   * Calculate time to execute call-by-name funtion
   */
  def time[R](description: String, block: => R): R = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    logger.info("{} - Elapsed time: {} ns", description, (t1 - t0).toString)
    result
  }

}
