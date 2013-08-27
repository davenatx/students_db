package dbproject

import scala.io.Source
import annotation.tailrec
import java.io.File
import java.io.PrintWriter
import com.typesafe.scalalogging.slf4j.Logging
import scala.language.postfixOps
import dbproject.dao._
import dbproject.domain._

/**
 * Read student data from a CSV file and write the data where each student is appended to the parent entry
 *
 * This removes duplicate Parent information represnted by
 * each Student
 */
object ConvertCSV extends Logging {

  // Convert line from CSV file to CSVEntry
  val csvEntryFunc = (line: String) => {
    logger.debug("CSVEntry: " + line)

    val data = line split (",")
    val studentId = (data(0) trim).toLong
    val campusName = (data(1) trim)
    val firstName = (data(2) trim)
    val lastName = (data(3) trim)
    val streetName = (data(4) trim)
    val apartment = (data(5) trim)
    val city = (data(6) trim)
    val state = (data(7) trim)

    /* Handle case where a record does not have the Zip */
    val zip = {
      if (data.length == 9) (data(8) trim)
      else ""
    }

    CSVEntry(studentId, campusName, firstName, lastName, streetName, apartment, city, state, zip)
  }

  /*
   * Parse CSV File and store in database
   */
  def processFile(fileName: String) {

    val printWriter = new PrintWriter(exportFileName)

    val csvLines: List[String] = (Source fromFile (fileName) getLines ()).toList

    processCSVLines(csvLines, printWriter)

    printWriter.flush
    printWriter.close
  }

  /*
   * Process CSV Lines and handle duplicate parent records representing multiple children
   */
  private def processCSVLines(csvLines: List[String], printWriter: PrintWriter) {
    val linesNoHeader = csvLines.tail
    val firstCSVEntry = csvEntryFunc(linesNoHeader.head)

    @tailrec
    def parseLine(lines: List[String], accu: List[CSVEntry], previous: CSVEntry) {
      lines match {
        case head :: tail => {
          val cvsEntry = csvEntryFunc(head)
          /* Accumulator is empty, place current cvsEntry in it */
          if (accu.isEmpty) parseLine(tail, cvsEntry :: accu, cvsEntry)
          /* Accumulator is not empty */
          else {
            /* Current entry has the same parent as previous, add to accumulator */
            if (cvsEntry.firstName == previous.firstName && cvsEntry.lastName == previous.lastName) parseLine(tail, cvsEntry :: accu, cvsEntry)
            /* Write the contents of the accumulator to the DB and place the current cvsEntry in new List */
            else {
              writeRecords(accu.reverse, printWriter)
              parseLine(tail, List(cvsEntry), cvsEntry)
            }
          }
        }
        case Nil => writeRecords(accu.reverse, printWriter)
      }
    }

    parseLine(linesNoHeader.tail, Nil, firstCSVEntry)
  }

  /**
   * Write CSVEntry records to the new file appending the student informatoin to the end
   * of the parent information
   */
  private def writeRecords(records: List[CSVEntry], printWriter: PrintWriter) {

    /* Insert the Student Records with the parentId */
    @tailrec
    def processStudents(recs: List[CSVEntry], acc: List[String]): List[String] = {
      recs match {
        case head :: tail => {

          val sInfo = "," + head.studentId + ",\"" + head.campusName + "\""

          processStudents(tail, sInfo :: acc)
        }
        case Nil => acc.reverse
      }
    }

    val firstRecord = records.head

    val parentInfo = "\"" + firstRecord.firstName + "\",\"" + firstRecord.lastName + "\",\"" + firstRecord.streetName + "\",\"" +
      firstRecord.apartment + "\",\"" + firstRecord.city + "\",\"" + firstRecord.state + "\",\"" + firstRecord.zip + "\""

    val studentInfo = processStudents(records, Nil).mkString("")

    logger.info("Parent: {}, \n\tStudent(s): {}", parentInfo, studentInfo)

    printWriter.println(parentInfo + studentInfo)
  }
}