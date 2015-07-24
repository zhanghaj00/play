package action

import actor.{AuthenticateResult, Authenticate, AuthActor}
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import controllers.ChatController._
import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.http.Status
import play.api.libs.concurrent.Akka
import play.api.mvc._
import akka.pattern.ask
import service.UserServiceImpl
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by zhanghao on 2015/7/20.
 */
class AuthenticateRequest {

}

case class AuthenticatedRequest[A](user: User, token: String, request: Request[A]) extends WrappedRequest(request)


trait AuthenticatedRequests{
  val serverSideSessionTokenKeyName = "token"

  //actor
  implicit val system = ActorSystem("YiQiRun")
  val authActor = system.actorOf(Props[AuthActor],name = "AuthAcotr")


  implicit val timeout = Timeout(1 second)


  val serverSideSessionExpirySec = Some(60 * 60 * 24)
  val loginForm = Form(
    mapping (
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )


  object AuthenticateForAll extends ActionBuilder[AuthenticatedRequest] {

    def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]):Future[Result] = {
      (authActor ? Authenticate(request)).mapTo[AuthenticateResult].flatMap{
        result =>
        if(result.valid){
          println("true")
          block(AuthenticatedRequest[A](new User(result.userName.get,""),"", request))
        }else{
          println("false")
          block(AuthenticatedRequest[A](new User("",""),"", request))
        }
      }.recover{
        case e =>Results.Status(Status.INTERNAL_SERVER_ERROR)
      }

    }
  }

  object AuthenticateFirst extends ActionBuilder[Request]{
    def invokeBlock[A](request:Request[A],block:Request[A] =>Future[Result]):Future[Result] = {
      println("success")
      loginForm.bindFromRequest()(request).fold(

        //处理错误
        errors => Future.successful(BadRequest(errors.errorsAsJson)),
        loginData =>UserServiceImpl.authenticate(loginData.name,loginData.password).map{user=>
          println(loginData.name)
          Created.withNewSession
          request.session.+("auth"->loginData.name)
          block(request).map{result=>
            result.withSession("auth"->loginData.name,"username"->loginData.name)
          }
        }getOrElse{
          Future.successful(Unauthorized("Invalid user/password"))
        }
      )
    }
  }

}
