package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current

case class LotrCard01(
  id: Int,
  name: String,
  typeId01: Int,
  sphereId: Int,
  threatCost: Option[Int],
  cardText: String,
  setId: Int,
  number: Int
) {
  def getParam() = Seq(
      "name" -> toParameterValue(name),
      "typeId01" -> toParameterValue(typeId01),
      "sphereId" -> toParameterValue(sphereId),
      "threatCost" -> toParameterValue(threatCost),
      "cardText" -> toParameterValue(cardText),
      "setId" -> toParameterValue(setId),
      "number" -> toParameterValue(number)
  )
}

object LotrCard01 {
  
  def getForm = Form (
    mapping(
      "id" -> default(number, -1),
      "name" -> text,
      "typeId01" -> number,
      "sphereId" -> number,
      "threatCost" -> optional(number),
      "cardText" -> text,
      "setId" -> number,
      "number" -> number
    )(LotrCard01.apply)(LotrCard01.unapply)
  )
  
  def all = DB.withConnection { implicit con =>
    SQL("select count(*) cnt from lotr_card").single(long("cnt"))
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
          """).on(card.getParam() :_*).executeUpdate()
    }
  }
}