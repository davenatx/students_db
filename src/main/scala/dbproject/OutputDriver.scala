package dbproject

import com.typesafe.scalalogging.slf4j.Logging

/**
 * Output the contents of the database to a CSV file
 */
object OutputDriver extends Logging {
  def main(args: Array[String]) {

    logger.info("Writing output file: {}", exportFileName)

    OutputCSV.createCSV
  }
}