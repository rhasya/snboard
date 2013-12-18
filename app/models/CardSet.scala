package models

import anorm._
import anorm.SqlParser._

import play.api._
import play.api.db._
import play.api.Play.current

case class CardSet(
  cardSet: String,
  cardSetName: String
)

object CardSet {
  val cardSetParser = str("card_set") ~ str("card_set_name") map {
    case cardSet~cardSetName => CardSet(cardSet, cardSetName)
  }
  
  def all: List[CardSet] = DB.withConnection { implicit conn =>
    SQL("select card_set, card_set_name from cardset order by seq").as(cardSetParser *)
  }
  
  def allData: Map[String, String] = (Map[String, String]() /: all)((m, c) => m + (c.cardSet -> c.cardSetName))
}