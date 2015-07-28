package service

import models.User

/**
 * Created by zhanghao on 2015/7/20.
 */
trait UserService {
  def authenticate(name: String, password: String): Option[User]
}


object UserServiceImpl extends UserService{

  val users = Map(
  "bo@163.com"->"123",
  "ni@163.com"->"123"
  )
  def checkPassword(name: String, password: String): Boolean = {
    users.get(name).filter { _ == password }.isDefined
  }


  def authenticate(name: String, password: String): Option[User] = {
    if (checkPassword(name, password)) Some(User(name,password))
    else None
  }
}
