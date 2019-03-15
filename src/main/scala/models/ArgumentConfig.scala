package models

import java.io.File
import java.time.LocalDate

case class ArgumentsDescription(description: Seq[String], argOption: Option[Arguments])



case class ArgumentOption(desc: String,
                          file: Option[File],
                          dateStringOption: Option[String],
                          dateOption: Option[LocalDate])
{

  def na(): Boolean = file.isEmpty && dateStringOption.isEmpty && dateOption.isEmpty

}