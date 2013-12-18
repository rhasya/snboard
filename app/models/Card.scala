package models

import anorm._
import anorm.SqlParser._

import play.api._
import play.api.db._
import play.api.Play.current

/* CASE CLASS */
case class Card(
  id: Long, 
  name: String,
  cardText: String,
  cardSet: String,
  cardNumber: Int,
  cardTypeId: Int
) {
  def getParam() : Seq[(Any, ParameterValue[_])] = {
    this.getClass().getDeclaredFields() map { f =>
      f.setAccessible(true)
      f.getName -> toParameterValue(f.get(this))
    }
  }
}

object Card {
  /** PARSER 1 */
  val cardParser1 = 
    long("id") ~ str("name") ~ str("card_text") ~ str("card_set") ~ int("card_number") ~ int("card_type_id") map {
      case id~name~cardText~cardSet~cardNumber~cardTypeId
    	  => Card(id, name, cardText, cardSet, cardNumber, cardTypeId)
    }
  
  /**
   * Get all data
   */
  def all: Seq[Card] = DB.withConnection { implicit conn =>
    SQL("select * from card").as(cardParser1 *)
  }

  /**
   * Get a specific card
   * @param id Card ID
   */
  def getCardById(id: Long): Card = DB.withConnection { implicit conn =>
    SQL("""select * from card where id = {id}""").on('id -> id).as(cardParser1.single)
  }
  
  /**
   * Save (Insert or Update) a card
   */
  def multi(card: Card) {
    DB.withConnection { implicit conn =>
      var cnt = SQL("select count(*) cnt from card where id = {id}").on('id -> card.id).as(long("cnt").single)
      if (cnt == 0) {
        SQL("""insert into card(name, card_text, card_set, card_number, card_type_id) 
          values ({name}, {cardText}, {cardSet}, {cardNumber}, {cardTypeId})""").on(card.getParam():_*).executeUpdate()
      }
      else {
        SQL("""update card set name = {name}, card_text = {cardText}, 
               card_set = {cardSet}, card_number = {cardNumber}, card_type_id = {cardTypeId} 
            where id = {id}""").on(card.getParam():_*).executeUpdate()
      }
    }
  }
  
  /**
   * Delete a card
   */
  def deleteCard(id: Long) {
    DB.withConnection { implicit conn =>
      SQL("delete from card where id = {id}").on('id -> id).executeUpdate()
    }
  }
}