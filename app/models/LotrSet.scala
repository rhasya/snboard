package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

import scala.language.postfixOps

object LotrSet {
  val parser = int("id") ~ str("name") map {
    case id ~ name => (id.toString(), name)
  }
  
  def all = DB.withConnection { implicit con =>
    SQL("select * from lotr_set order by seq").as(parser *).toSeq
  }
}