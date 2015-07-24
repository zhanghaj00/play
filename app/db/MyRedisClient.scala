package db

import com.redis.{RedisClient}

/**
 * Created by zhanghao on 2015/7/24.
 */
class MyRedisClient  extends OperationForRedis{

 // implicit val akkaSystem = akka.actor.ActorSystem()
 // implicit lazy val redis = new RedisClient("127.0.0.1",6379)

 // println(redis.get("firendList:ni@163.com"))

  def createRedis = {
    val client = new RedisClient("127.0.0.1",6379)
    client
  }


  def withClient[T](body: RedisClient => T) = {
    val client = createRedis
    body(client)

  }

}

object MyRedisClient extends App{
  def apply = {new MyRedisClient()}

  val client = this.apply.createRedis

  println(client.get("firendList:ni@163.com"))
}