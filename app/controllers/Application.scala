package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object Application extends Controller {

  val cardForm = Form(
    mapping(
      "id" -> default(longNumber, 0L),
      "name" -> nonEmptyText,
      "cardText" -> text,
      "cardSet" -> nonEmptyText,
      "cardNumber" -> number,
      "cardTypeId" -> number
    )(Card.apply)(Card.unapply)
  )

  // Application Start
  def index = Action {
    Redirect(routes.Application.list)
  }
  
  def list = Action {
  	Ok(views.html.list(Card.all, CardSet.allData))
  }
  
  def card(id: Long) = Action {
    val card = Card.getCardById(id)
    Ok(views.html.card(card, CardSet.allData, CardType.allData))
  }

  def register = Action {
  	Ok(views.html.register(cardForm, CardSet.allData, CardType.allData))
  }

  def regCard = Action { implicit request =>
  	// Form으로부터 자료를 얻는다.
  	cardForm.bindFromRequest.fold(
  	  error => {
  	  	BadRequest(views.html.register(error, CardSet.allData, CardType.allData))
  	  },
  	  card => {
  	  	// 등록 처리를 한다.
  	  	Card.multi(card)
  	  	Redirect(routes.Application.list())
  	  }
  	)
  }
  
  def modify(id: Long) = Action {
    // Select card
    val card : Card = Card.getCardById(id)
    // Fill form
    val filledCardForm = cardForm.fill(card)
    Ok(views.html.register(filledCardForm, CardSet.allData, CardType.allData))
  }
  
  def delete(id: Long) = Action {
    Card.deleteCard(id)
    Redirect(routes.Application.list)
  }
}