package actor

import akka.actor._
import play.api.libs.json.JsValue

/**
 * Created by zhanghao on 2015/7/26.
 */
class ChatUserActor(name:String,out: ActorRef,system:ActorSystem) extends Actor with ActorLogging{

  //val system = ActorSystem("chatSystem")



  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    val chatUser = system.actorOf(Props(new ChatUserActorList(out)),name = "chatUser:"+name)
  }

  def receive = {
    case msg:String=>
      println("first time getMSG:"+msg)
      val sender = system.actorSelection("akka://YiQiRun/user/chatUser:bo@163.com")
      sender ! msg


    case jsvalue:JsValue =>
      val atoName = (jsvalue \ "ato").as[String]
      val msg = (jsvalue \ "message").as[String]
      println("first time getMSG:"+msg)
      val sender = system.actorSelection("akka://YiQiRun/user/chatUser:"+atoName)
      sender ! msg
    case _=> println("okokokoko")
  }
  override def postStop() = {
    val sender = system.actorSelection("akka://YiQiRun/user/chatUser:"+name)
    sender ! PoisonPill
    println("closed"+name)
  }
}
object ChatUserActor{

    def props(name:String,out: ActorRef,system:ActorSystem) = Props(new ChatUserActor(name,out,system))

}



class ChatUserActorList(out: ActorRef) extends Actor{

  val outer = out
  def receive = {
    case msg:String=>
      println("I got msg")
      outer ! "I got msg"+msg
  }
}