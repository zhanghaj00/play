package controllers

import models.User
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api._
import play.api.mvc.{Session => _, _}
import play.api.Play.current
import views.html

object ChatController extends Controller {

  /**
   * Main route. Redirects to the login page
   */
  def enter = Action { implicit  request=>
    //Redirect(routes.ChatController.prepareLogin)

    Ok(views.html.enter(LoginForm))
  }

  val LoginForm = Form(
    mapping (
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )

  def login = Action{ implicit  request=>
    Ok(views.html.login())
  }
  /**
   * Login page
   */

  def prepareLogin = Action { implicit request =>
    println("success")
    LoginForm.bindFromRequest.fold(
    //处理错误
    errors => Ok(views.html.result("nihao")("sb")), //BadRequest(views.html.index(LoginForm))
    success = {
      case (user) =>
        println("success"+user.name)
        //发言
        //Message.post(name, content)
        //重新定向到显示留言列表和发言表单页面
        Created.withSession("username"->user.name)
        Ok(views.html.result(user.name)(user.password))
    }
    )
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
