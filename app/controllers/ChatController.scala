package controllers

import action.AuthenticatedRequests
import actor.{GetFriendinvitation, GetFriendListEvent, UserActor}
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
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
import scala.concurrent.ExecutionContext.Implicits.global
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

  def friendList = AuthenticateForAll.async{ implicit request =>
    implicit val timeout:Timeout = 5.seconds
    (UserActor.userActor ? GetFriendListEvent("friendlist:zhanghao")).mapTo[Map[String,String]].map{ //request.session.get("userName").get
        friendMap => println(friendMap.get("hello").get)
        Ok(views.html.login(request))

    }
   /* val userName = request.session.get("userName").get
    val friendList = TODO //TODO Vector 得到list


    Ok(views.html.chat)*/
  }

  def addfriend(username:String) = AuthenticateForAll.async { implicit request =>

   // val username = request.body.asText.get
    //通过username得到用户的detail 返回成功 用ajax 来请求这个借接口

    val askUsername = request.session.get("username").get
    val useractor = system.actorSelection("akka://YiQiRun/user/user:"+username)


    (useractor ? GetFriendinvitation(askUsername)).mapTo[String].map{
      result =>
        Ok(result)
    }
  }
}
