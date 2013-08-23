package dbproject.dao

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import slick.jdbc.meta.MTable

import com.typesafe.scalalogging.slf4j.Logging
import dbproject._
import dbproject.domain.{ Student, Students, Parent }

class StudentDAO extends Logging {

  /**
   * Create the Students table if it does not exist
   */
  def createTable = {
    db withSession {
      if (MTable.getTables(studentsTableName).list().isEmpty) {
        logger.info("Create {} SQL: {}", studentsTableName, Students.ddl.createStatements.mkString("\n"))
        Students.ddl.create
      } else logger.warn("{} table already exists!", studentsTableName)
    }
  }

  /**
   * Drop the Students table if it exists
   */
  def dropTable = {
    db withSession {
      if (!MTable.getTables(studentsTableName).list().isEmpty) {
        logger.info("Drop {} SQL: {}", studentsTableName, Students.ddl.dropStatements.mkString("\n"))
        Students.ddl.drop
      } else logger.warn("{} table does not exist!", studentsTableName)
    }
  }

  /**
   * Save Student to database
   *
   * @param student   Student to insert
   */
  def create(student: Student) {
    db withSession {
      Students insert student
    }
  }

  /**
   * Retrieve list of Student by the parent id
   *
   * @param parent    Parent object
   * @return list     List of Student belonging to this parent
   */
  def findByParent(parent: Parent): List[Student] = {
    db withSession {
      val id = parent.id.getOrElse(-1L)
      logger.debug("id: " + id)

      val q = for {
        s <- Students if s.parId === id
      } yield s

      logger.debug("SQL Query: {}", q.selectStatement)
      q.list
    }
  }
}