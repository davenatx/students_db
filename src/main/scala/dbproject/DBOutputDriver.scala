package dbproject

import com.typesafe.scalalogging.slf4j.Logging

/**
 * Output the contents of the database to a CSV file
 */
object DBOutputDriver extends Logging {
  def main(args: Array[String]) {

    logger.info("Writing output file: {}", exportFileName)

    time("Database Output Process", DBOutputCSV.createCSV)
  }
}