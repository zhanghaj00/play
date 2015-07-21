package controllers

import action.AuthenticatedRequests
import models.User
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api._
import play.api.mvc.{Session => _, _}
import play.api.Play.current
import service.UserServiceImpl
import views.html

object ChatController extends Controller with AuthenticatedRequests{

  /**
   * Main route. Redirects to the login page
   */
  def enter = Action { implicit  request=>
    //Redirect(routes.ChatController.prepareLogin)

    Ok(views.html.enter(loginForm))
  }



  def login = Action{ implicit  request=>
    Ok(views.html.login(request))
  }
  /**
   * Login page
   */

  def prepareLogin = AuthenticateFirst { implicit request =>
    Ok(views.html.result(request.session.get("auth").isDefined.toString)(""))
  }

  def contactMe = AuthenticateForAll { implicit request =>
    Ok(views.html.contact(request.request))

  }
  /**
   * If login is ok, redirects to the chat page
   */

 /* def login = Action{
    Redirect(routes.ChatController.prepareLogin)
  }*/
/*
 implicit request =>
      LoginForm.bindFromRequest.fold(
        errors => BadRequest(views.html.login(errors)),
        user => Redirect(routes.ChatController.chat(user))
      )

 */
  /**
   * Chat page
   */
 /* def chat(user:User) = Action {
    Ok(views.html.chat(user))
  }*/

  /**
   * Websocket entry point using actors
   */
  def websocket(user:User) = WebSocket.acceptWithActor[JsValue, JsValue] {
    request => out =>
      ChatActor.props(user, out)
  }
}
