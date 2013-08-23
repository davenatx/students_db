package dbproject.domain

import scala.slick.driver.H2Driver.simple._
import com.typesafe.scalalogging.slf4j.Logging
import dbproject._

/**
 * Student Entity
 *
 * The id is an Option becuase it is autoincrimented by the Database
 *
 * @parm id         student id
 * @parm parentId   parent id
 * @parm campusName campus name
 */
case class Student(id: Long, parentId: Long, campusName: String)

/**
 * Mapped STUDENTS table
 */
object Students extends Table[Student](studentsTableName) {
  def id = column[Long]("STU_ID")
  def parId = column[Long]("PAR_ID")
  def campusName = column[String]("CAMPUS_NAME")

  def * = id ~ parId ~ campusName <> (Student, Student.unapply _)
  // Foreign Key
  def parent = foreignKey("PAR_FK", parId, Parents)(_.id)
}