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
    get[Option[Int]]("type_id_02") ~
    int("sphere_id") ~
    get[Option[String]]("threat_cost") ~
    get[Option[String]]("will_threat") ~
    get[Option[String]]("attack") ~
    get[Option[String]]("defense") ~
    get[Option[String]]("hitpoint") ~
    get[Option[String]]("card_text") ~
    get[Option[String]]("card_text_ko") ~
    get[Option[String]]("flavor_text") ~
    get[Option[String]]("flavor_text_ko") ~
    int("set_id") ~
    int("number") ~
    int("quantity") ~
    get[Option[String]]("illustrator") map {
    case id~name~typeId01~typeId02~sphereId~threatCost~
    	willThreat~attack~defense~hitpoint~cardText~cardTextKo~
    	flavorText~flavorTextKo~setId~number~quantity~illustrator =>
    	  LotrCard01(id, name, typeId01, typeId02, sphereId, threatCost,
    	      willThreat, attack, defense, hitpoint, cardText, 
    	      cardTextKo, flavorText, flavorTextKo, setId, number, quantity, 
    	      illustrator)
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
    val sql = """
      insert into lotr_card(
            name
          , type_id_01
          , type_id_02
          , sphere_id
          , threat_cost
          , will_threat
          , attack
          , defense
          , hitpoint
          , card_text
          , card_text_ko
          , set_id
          , number)
      values ({name}
        , {typeId01}
        , {typeId02}
        , {sphereId}
        , {threatCost}
        , {willThreat}
        , {attack}
        , {defense}
        , {hitpoint}
        , {cardText}
        , {cardTextKo}
        , {setId}
        , {number})
      """
    DB.withConnection { implicit con =>
      SQL(sql).on(card.getParam() :_*).executeInsert()
    }
  }
  
  def update(card: LotrCard01) {
    DB.withConnection { implicit con =>
      SQL("""update lotr_card set
               name = {name},
               type_id_01 = {typeId01},
               type_id_02 = {typeId02},
               sphere_id = {sphereId},
               threat_cost = {threatCost},
               will_threat = {willThreat},
               attack = {attack},
               defense = {defense},
               hitpoint = {hitpoint},
               card_text = {cardText},
               card_text_ko = {cardTextKo},
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