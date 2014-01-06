package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current

import scala.language.postfixOps

case class LotrCard01(
  id: Int,
  name: String,
  typeId01: Int,
  sphereId: Int,
  threatCost: Option[Int],
  cardText: String,
  setId: Int,
  number: Int,
  typeName01: String = null,
  sphereName: String = null,
  setName: String = null
) {
  def getParam() = Seq(
      "id" -> toParameterValue(id),
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
  
  val rowParser = int("id") ~ 
    		str("name") ~ 
    		int("type_id_01") ~
    		int("sphere_id") ~
    		int("threat_cost") ~
    		str("card_text") ~
    		int("set_id") ~
    		int("number") ~
    		str("type_name_01") ~
    		str("sphere_name") ~
    		str("set_name") map {
    case id ~ name ~ typeId01 ~ 
      sphereId ~ threatCost ~ cardText ~
      setId ~ number ~ typeName01 ~
      sphereName ~ setName => {
        var cardText2 = cardText.replaceAll("\r\n", "<br>")
        LotrCard01(id, name, typeId01, sphereId, Some(threatCost), cardText2, setId, number, typeName01, sphereName, setName)
      }
  }
  
  def getForm = Form (
    mapping(
      "id" -> default(number, -1),
      "name" -> text,
      "typeId01" -> number,
      "sphereId" -> number,
      "threatCost" -> optional(number),
      "cardText" -> text,
      "setId" -> number,
      "number" -> number,
      "typeName01" -> ignored(""),
      "sphereName" -> ignored(""),
      "setName" -> ignored("")
    )(LotrCard01.apply)(LotrCard01.unapply)
  )
  
  def all = DB.withConnection { implicit con =>
    
    SQL("""select a.*
                , b.name as type_name_01
                , c.name as sphere_name
                , d.name as set_name
           from lotr_card a, lotr_type b, lotr_sphere c, lotr_set d
           where a.type_id_01 = b.id
           and a.sphere_id = c.id
           and a.set_id = d.id
           order by a.set_id, a.number""").as(rowParser*)
  }
  
  def getCard(id: Long) = DB.withConnection { implicit con =>
    SQL("""select a.*
                , b.name as type_name_01
                , c.name as sphere_name
                , d.name as set_name
           from lotr_card a, lotr_type b, lotr_sphere c, lotr_set d
           where a.type_id_01 = b.id
           and a.sphere_id = c.id
           and a.set_id = d.id
           and a.id = {id}
           order by a.set_id, a.number""").on("id" -> id).single(rowParser)
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