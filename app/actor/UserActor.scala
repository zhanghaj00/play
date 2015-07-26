package actor

import akka.actor.{Props, ActorSystem, Actor, ActorLogging}
import db.MyRedisClient
import play.api.libs.iteratee.Concurrent
import scala.concurrent.duration._
/**
 * Created by zhanghao on 2015/7/24.
 */
class UserActor extends Actor with ActorLogging{

  private[this] val (enumerator, channel) = Concurrent.broadcast[String]


  def receive = {

    case GetFriendListEvent(name) =>
      val client = MyRedisClient.apply.createRedis
      if(client.exists(name)){
          val friendlist = client.hgetall(name).get
         sender() ! friendlist
      }

    case GetFriendinvitation(askUsername:String) =>
      //把这个好友请求加入到redis队列中 用一个friendAskRequest domain 来放这个请求
      log.info(askUsername+": want add you to his friendList")
      sender ! "ok request had been send"

  }

}

object UserActor {
    val userActor = ActorSystem("UserActor").actorOf(Props[UserActor],"UserActor1")

}

sealed class EventForUser

case class GetFriendListEvent(userName:String) extends EventForUser
case class GetFriendinvitation(askUsername:String) extends EventForUser
