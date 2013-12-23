package controllers

import play.api._
import play.api.mvc._

import models._

object LotrCtrl extends Controller {

  def list = Action {
    Ok(views.html.lotrmain())
  }
  
  def newCard = Action {
    val form = LotrCard01.getForm
    Ok(views.html.lotreditcard(form, LotrType.all))
  }
  
  def save = Action { implicit request =>
    val form = LotrCard01.getForm
    form.bindFromRequest.fold(
      errorForm => BadRequest(views.html.lotreditcard(errorForm, LotrType.all)),
      success => {
        // SAVE
        Redirect(routes.LotrCtrl.list)
      }
    )
  }
}