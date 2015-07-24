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
    println("ok"+request.user.name)
    //Ok(views.html.contact(request.request))
    Redirect(routes.ChatController.chat)
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
  def chat = AuthenticateForAll { implicit request=>
    Ok(views.html.login(request))
  }

  /**
   * Websocket entry point using actors
   */
  def websocket = WebSocket.acceptWithActor[JsValue, JsValue] {
   implicit request => out =>
      ChatActor.props(request.session.get("username").get, out)
  }

  def logout = Action { implicit request =>
    //TODO: kick me from all rooms and destroy my own actor (terminate the user enumerator)
    Ok.withNewSession
  }


  //功能1.好友列表 2.找一个好友聊天  3.聊天室

  def friendList = AuthenticateForAll { implicit request =>

    val userName = request.session.get("userName").get
    val friendList = TODO //TODO Vector 得到list
    Ok(views.html.chat)
  }
}
