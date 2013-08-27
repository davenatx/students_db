package dbproject

import annotation.tailrec
import java.io.File
import java.io.PrintWriter
import com.typesafe.scalalogging.slf4j.Logging
import dbproject.dao._
import dbproject.domain._

/**
 * Output the contents of the database to a CSV file.
 * The Students are appended to the end of the Parent record.
 */
object DBOutputCSV extends Logging {

  def createCSV {
    val printWriter = new PrintWriter(exportFileName)

    val parentRecords = parentDao.getAllRecords

    processParents(parentRecords, printWriter)

    printWriter.flush
    printWriter.close
  }

  @tailrec
  private def processParents(list: List[Parent], printWriter: PrintWriter) {
    list match {
      case head :: tail => {

        val parentInfo = "\"" + head.firstName + "\",\"" + head.lastName + "\",\"" + head.streetName + "\",\"" + head.apartment + "\",\"" +
          head.city + "\",\"" + head.state + "\",\"" + head.zip + "\""

        // query students and write output
        val students = studentDao.findByParent(head)

        val studentInfo = processStudents(students, Nil).mkString("")

        logger.info("Parent: {}, \n\tStudent(s): {}", parentInfo, studentInfo)
        printWriter.println(parentInfo + studentInfo)

        processParents(tail, printWriter)
      }
      case Nil => logger.debug("Done processing parents")
    }
  }

  @tailrec
  private def processStudents(list: List[Student], acc: List[String]): List[String] = {
    list match {
      case head :: tail => {
        val studentInfo = "," + head.id + ",\"" + head.campusName + "\""
        processStudents(tail, studentInfo :: acc)
      }
      case Nil => acc.reverse
    }
  }
}