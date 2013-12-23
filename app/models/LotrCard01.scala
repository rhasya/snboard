package models

import play.api.data._
import play.api.data.Forms._

case class LotrCard01(
  id: Int,
  name: String,
  typeId01: Int,
  sphereId: Int,
  threatCost: Option[Int],
  cardText: String,
  setId: Int,
  number: Int
)

object LotrCard01 {
  
  def getForm = Form (
    mapping(
      "id" -> number,
      "name" -> text,
      "typeId01" -> number,
      "sphereId" -> number,
      "threatCost" -> optional(number),
      "cardText" -> text,
      "setId" -> number,
      "number" -> number
    )(LotrCard01.apply)(LotrCard01.unapply)
  )
}