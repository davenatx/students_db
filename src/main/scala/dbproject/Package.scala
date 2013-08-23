import com.typesafe.config.ConfigFactory
import scala.slick.driver.H2Driver.simple._
import dbproject.dao._

/**
 * Package Object containing global information
 */
package object dbproject {

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

}
