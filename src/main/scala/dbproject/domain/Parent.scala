package dbproject.domain

import scala.slick.driver.H2Driver.simple._
import com.typesafe.scalalogging.slf4j.Logging
import dbproject._

/**
 * Parent Entity
 *
 * The id is an Option becuase it is autoincrimented by the Database
 *
 * @parm id			unique id
 * @parm firstName	first name
 * @parm lastName	last name
 * @parm streetName	street name
 * @parm apartment	apartment
 * @parm city		city
 * @parm state		state
 * @parm zip		zip
 */
case class Parent(id: Option[Long], firstName: String, lastName: String, streetName: String, apartment: String, city: String,
  state: String, zip: String)

/**
 * Mapped PARENTS table
 */
object Parents extends Table[Parent](parentsTableName) {
  def id = column[Long]("PAR_ID", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("FIRST_NAME")
  def lastName = column[String]("LAST_NAME")
  def streetName = column[String]("STREET_NAME")
  def apartment = column[String]("APT")
  def city = column[String]("CITY")
  def state = column[String]("STATE")
  def zip = column[String]("ZIP")

  def * = id.? ~ firstName ~ lastName ~ streetName ~ apartment ~ city ~ state ~ zip <> (Parent, Parent.unapply _)

}