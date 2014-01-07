package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object LotrCtrl extends Controller {
  
  val form = Form (
    mapping(
      "id" -> default(number, -1),
      "name" -> nonEmptyText,
      "typeId01" -> number,
      "typeId02" -> optional(number),
      "sphereId" -> number,
      "threatCost" -> optional(text),
      "willThreat" -> optional(text),
      "attack" -> optional(text),
      "defense" -> optional(text),
      "hitpoint" -> optional(text),
      "cardText" -> optional(text),
      "cardTextKo" -> optional(text),
      "flavorText" -> optional(text),
      "flavorTextKo" -> optional(text),
      "setId" -> number,
      "number" -> number,
      "quantity" -> default(number, 3),
      "illustrator" -> optional(text)
    )(LotrCard01.apply)(LotrCard01.unapply)
  )
  
  // view list
  def list = Action {
    Ok(views.html.lotrmain(LotrCard02.all))
  }
  
  // view new Card
  def newCard = Action {
    Ok(views.html.lotreditcard(form, LotrType.all, LotrSphere.all, LotrSet.all))
  }
  
  // view modify Card
  def modifyCard(id: Long) = Action {
    val form2 = form.fill(LotrCard01.getCard(id))
    Ok(views.html.lotreditcard(form2, LotrType.all, LotrSphere.all, LotrSet.all))
  }
  
  // view single Card
  def card(id: Long) = Action {
    val card = LotrCard02.getCard(id)
    Ok(views.html.lotrviewcard(card))
  }
  
  def save = Action { implicit request =>
    form.bindFromRequest.fold(
      errorForm => BadRequest(views.html.lotreditcard(errorForm, LotrType.all, LotrSphere.all, LotrSet.all)),
      success => {
        // 존재하는 자료인지 확인한다.
        if(LotrCard01.checkUnique(success)) {
          // 저장한다.
          LotrCard01.save(success)
          // 이전 화면으로 되돌린다.
          Redirect(routes.LotrCtrl.list)
        }
        else if (success.id != -1) {
          LotrCard01.update(success)
          Redirect(routes.LotrCtrl.list)
        }
        else {
          val errForm = form.fill(success).withGlobalError("Already Exists")
          BadRequest(views.html.lotreditcard(errForm, LotrType.all, LotrSphere.all, LotrSet.all))
        }
      }
    )
  }
  
  // delete Card
  def delete(id: Long) = Action { implicit request =>
    case class Id(id: Int)
    val form = Form (
      mapping("id" -> number)(Id.apply)(Id.unapply)
    )
    form.bindFromRequest.fold(
      errorForm => Redirect(routes.LotrCtrl.card(errorForm.get.id)),
      idObj => {
        LotrCard01.delete(idObj.id)
        Redirect(routes.LotrCtrl.list)
      }
    )
  }
}