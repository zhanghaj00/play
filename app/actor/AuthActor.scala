package actor

import akka.actor.Actor
import models.User
import play.api.mvc.Request

/**
 * Created by zhanghao on 2015/7/20.
 */
class AuthActor extends Actor{

  def receive = {
    case Authenticate(request) =>
      if(request.session.get("auth").isDefined){
        sender ! AuthenticateResult(true,Option(request.session.get("auth").get))
      }else{
        sender ! AuthenticateResult(false)
      }
  }

}

case class Authenticate[A](request:Request[A])

case class AuthenticateResult(valid:Boolean,userName: Option[String]=None)
