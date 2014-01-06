package controllers

import play.api._
import play.api.mvc._
import models._
import views.html.defaultpages.badRequest

object LotrCtrl extends Controller {

  def list = Action {
    Ok(views.html.lotrmain(LotrCard01.all))
  }
  
  def newCard = Action {
    val form = LotrCard01.getForm
    Ok(views.html.lotreditcard(form, LotrType.all, LotrSphere.all, LotrSet.all))
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
        else {
          BadRequest(views.html.lotreditcard(form.fill(success), LotrType.all, LotrSphere.all, LotrSet.all))
        }
      }
    )
  }
}