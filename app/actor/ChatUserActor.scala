package actor

import akka.actor._

/**
 * Created by zhanghao on 2015/7/26.
 */
class ChatUserActor(name:String,out: ActorRef) extends Actor with ActorLogging{

  val system = ActorSystem("chatSystem")
  val chatUser = system.actorOf(Props(new ChatUserActorList(out)),name = "chatUser:"+name)

  def receive = {
    case msg:String=>
      val sender = system.actorSelection("akka://chatSystem/user/chatUser:ni@163.com")
      sender ! msg
  }

}
object ChatUserActor{

    def props(name:String,out: ActorRef) = Props(new ChatUserActor(name,out))

}



class ChatUserActorList(out: ActorRef) extends Actor{

  def receive = {
    case msg:String=>
      out ! "I got msg"+msg
  }
}