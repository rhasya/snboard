package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current

import scala.language.postfixOps

case class LotrCard01 (
  id: Int,
  name: String,
  typeId01: Int,
  typeId02: Option[Int] = None,
  sphereId: Int,
  threatCost: Option[String] = None,
  willThreat: Option[String] = None,
  attack: Option[String] = None,
  defense: Option[String] = None,
  hitpoint: Option[String] = None,
  cardText: Option[String] = None,
  cardTextKo: Option[String] = None,
  flavorText: Option[String] = None,
  flavorTextKo: Option[String] = None,
  setId: Int,
  number: Int,
  quantity: Int,
  illustrator: Option[String] = None
) extends DVO

object LotrCard01 {
  val rowParser =
    int("id") ~
    str("name") ~
    int("type_id_01") ~
    int("type_id_02") ~
    int("sphere_id") ~
    str("threat_cost") ~
    str("will_threat") ~
    str("attack") ~
    str("defense") ~
    str("hitpoint") ~
    str("card_text") ~
    str("card_text_ko") ~
    str("flavor_text") ~
    str("flavor_text_ko") ~
    int("set_id") ~
    int("number") ~
    int("qunatity") ~
    str("illustrator") map {
    case id~name~typeId01~typeId02~sphereId~threatCost~
    	willThreat~attack~defense~hitpoint~cardText~cardTextKo~
    	flavorText~flavorTextKo~setId~number~quantity~illustrator =>
    	  LotrCard01(id, name, typeId01, Some(typeId02), sphereId, Some(threatCost),
    	      Some(willThreat), Some(attack), Some(defense), Some(hitpoint), Some(cardText), 
    	      Some(cardTextKo), Some(flavorText), Some(flavorTextKo), setId, number, quantity, 
    	      Some(illustrator))
  }
  
  def getCard(id: Long) = DB.withConnection {implicit con =>
    val sql = """
      select a.*
      from lotr_card a
      where a.id = {id}"""
      
    SQL(sql).on('id -> id).single(rowParser)
  }
  
  def checkUnique(card: LotrCard01) = DB.withConnection { implicit con =>
    val cnt = SQL("select count(*) cnt from lotr_card where set_id = {setId} and number = {number}")
    	.on('setId -> card.setId, 'number -> card.number).single(long("cnt"))
    if (cnt > 0) false else true
  }
  
  def save(card: LotrCard01) {
    DB.withConnection { implicit con =>
      SQL("""insert into lotr_card(name, type_id_01, sphere_id, threat_cost, card_text, set_id, number)
             values ({name}, {typeId01}, {sphereId}, {threatCost}, {cardText}, {setId}, {number})
          """).on(card.getParam() :_*).executeInsert()
    }
  }
  
  def update(card: LotrCard01) {
    DB.withConnection { implicit con =>
      SQL("""update lotr_card set
               name = {name},
               type_id_01 = {typeId01},
               sphere_id = {sphereId},
               threat_cost = {threatCost},
               card_text = {cardText},
               set_id = {setId},
               number = {number}
             where id = {id}""").on(card.getParam():_*).executeUpdate()
    }
  }
  
  def delete(id: Int) {
    DB.withConnection { implicit con =>
      SQL("delete from lotr_card where id = {id}").on("id" -> id).executeUpdate()
    }
  }
}