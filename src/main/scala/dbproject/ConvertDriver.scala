package dbproject

import java.io.File
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Drop the tables, create the tables, and perform the import from a CSV file
 * located in the root directory
 */
object ConvertDriver extends Logging {
  def main(args: Array[String]) {

    // Retrieve Array of files ending with .csv in the root directory
    val files = new File(".").list.filter(_.endsWith(".csv"))

    // If no .csv files were found, alert the user and exit
    if (files.isEmpty) {
      logger warn ("No .csv File Found!")
      System.exit(0)
    }

    // If multiple .csv files were found, alert the user and exit
    if (files.length > 1) {
      logger warn ("Multiple .csv Files Found!")
      System.exit(0)
    }

    // Process only csv file in the array
    logger info ("Processing CSV File: " + files(0))

    time("Conversion Process", ConvertCSV.processFile(files(0)))
  }
}