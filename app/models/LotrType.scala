package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

object LotrType {
  val parser = int("id") ~ str("name") map {
    case id ~ name => (id.toString(), name)
  }
  
  def all = DB.withConnection { implicit con =>
    SQL("select * from lotr_type order by id").as(parser *).toSeq
  }
}