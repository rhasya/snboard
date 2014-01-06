package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object LotrCtrl extends Controller {
  // view list
  def list = Action {
    Ok(views.html.lotrmain(LotrCard01.all))
  }
  
  // view new Card
  def newCard = Action {
    val form = LotrCard01.getForm
    Ok(views.html.lotreditcard(form, LotrType.all, LotrSphere.all, LotrSet.all))
  }
  
  // view modify Card
  def modifyCard(id: Long) = Action {
    var form = LotrCard01.getForm
    form = form.fill(LotrCard01.getCard(id))
    Ok(views.html.lotreditcard(form, LotrType.all, LotrSphere.all, LotrSet.all))
  }
  
  // view single Card
  def card(id: Long) = Action {
    val card = LotrCard01.getCard(id)
    Ok(views.html.lotrviewcard(card))
  }
  
  def save = Action { implicit request =>
    val form = LotrCard01.getForm
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