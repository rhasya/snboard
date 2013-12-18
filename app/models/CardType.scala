package models

import anorm._
import anorm.SqlParser._

import play.api._
import play.api.db._
import play.api.Play.current

case class CardType(
  id: Long,
  name: String
)

object CardType {
  
  val cardTypeParser1 = long("id") ~ str("name") map {
    case id~name => CardType(id, name)
  }
  
  def all: List[CardType] = DB.withConnection { implicit conn =>
    SQL("select id, name from cardtype order by seq").as(cardTypeParser1 *)
  }
  
  def allData: Map[String, String] = (Map[String, String]() /: this.all)((m, c) => m + (s"{c.id}" -> c.name))
}