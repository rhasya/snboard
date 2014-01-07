package models

import anorm._
import anorm.SqlQuery._
import anorm.SqlParser._

import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current

import scala.language.postfixOps

case class LotrCard02(
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
  illustrator: Option[String] = None,
  typeName01: String,
  sphereName: String,
  setName: String
)

object LotrCard02 {
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
    get[Option[String]]("illustrator") ~
    str("type_name_01") ~
    str("sphere_name") ~
    str("set_name") map {
    case id~name~typeId01~typeId02~sphereId~threatCost~
    	willThreat~attack~defense~hitpoint~cardText~cardTextKo~
    	flavorText~flavorTextKo~setId~number~quantity~illustrator~
    	typeName01~sphereName~setName =>
    	  LotrCard02(id, name, typeId01, typeId02, sphereId, threatCost,
    	      willThreat, attack, defense, hitpoint, cardText, 
    	      cardTextKo, flavorText, flavorTextKo, setId, number, quantity, 
    	      illustrator, typeName01, sphereName, setName)
  }
  
  def all = DB.withConnection { implicit con =>
    val sql = """
      select a.*
           , b.name as type_name_01
           , c.name as sphere_name
           , d.name as set_name
      from lotr_card a, lotr_type b, lotr_sphere c, lotr_set d
      where a.type_id_01 = b.id
      and a.sphere_id = c.id
      and a.set_id = d.id
      order by a.set_id, a.number"""
    
    SQL(sql).as(rowParser*)
  }
  
  def getCard(id: Long) = DB.withConnection {implicit con =>
    val sql = """
      select a.*
           , b.name as type_name_01
           , c.name as sphere_name
           , d.name as set_name
      from lotr_card a, lotr_type b, lotr_sphere c, lotr_set d
      where a.type_id_01 = b.id
      and a.sphere_id = c.id
      and a.set_id = d.id
      and a.id = {id}"""
      
    SQL(sql).on('id -> id).single(rowParser)
  }
}