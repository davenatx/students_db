package dbproject.dao

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import slick.jdbc.meta.MTable

import com.typesafe.scalalogging.slf4j.Logging
import dbproject._
import dbproject.domain.{ Parent, Parents }

class ParentDAO extends Logging {

  /**
   * Create the Parents table if it does not exist
   */
  def createTable = {
    db withSession {
      if (MTable.getTables(parentsTableName).list().isEmpty) {
        logger.info("Create {} SQL: {}", parentsTableName, Parents.ddl.createStatements.mkString("\n"))
        Parents.ddl.create
      } else logger.warn("{} table already exists!", parentsTableName)
    }
  }

  /**
   * Drop the Parents table if it exists
   */
  def dropTable = {
    db withSession {
      if (!MTable.getTables(parentsTableName).list().isEmpty) {
        logger.info("Drop {} SQL: {}", parentsTableName, Parents.ddl.dropStatements.mkString("\n"))
        Parents.ddl.drop
      } else logger.warn("{} table does not exist!", parentsTableName)
    }
  }

  /**
   * Saves Parent Entity into database.
   *
   * @param   parent 	parent entity to insert
   * @return  id of newly created entity
   */
  def create(parent: Parent): Long = {
    db withSession {
      Parents returning Parents.id insert parent
    }
  }

  /**
   * Retrieve all Parent records in the Parents table
   *
   * @return list of Parent records
   */
  def getAllRecords: List[Parent] = {
    db withSession {
      val q = for {
        p <- Parents
      } yield p
      logger.debug("SQL Query: {}", q.selectStatement)
      q.list
    }
  }
}