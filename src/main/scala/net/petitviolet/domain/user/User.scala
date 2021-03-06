package net.petitviolet.domain.user

import net.petitviolet.domain.support.{ Entity, ID }
import spray.json._

import scala.language.implicitConversions

case class User(id: ID[User], name: Name, email: Email) extends Entity[ID[User]]

case class Name(value: String) extends AnyVal
case class Email(value: String) extends AnyVal

object UserJsonProtocol extends DefaultJsonProtocol {
  //   unnecessary format
  //  final case class Users(users: Seq[User])
  //  implicit def toUsers(users: Seq[User]): Users = Users(users)
  //  implicit val usersFormat: RootJsonFormat[Users] = jsonFormat1(Users.apply)

  // below jsonFormat behave as not expected
  //  implicit val userIdFormat = jsonFormat(ID.apply[User] _, "value")
  //  implicit val nameFormat = jsonFormat(Name.apply _, "value")
  //  implicit val emailFormat = jsonFormat(Email.apply _, "value")
  //  implicit val userFormat = jsonFormat(User.apply, "id", "name", "email")
  //  implicit val userIdFormat = jsonFormat1(ID.apply[User])
  //  implicit val nameFormat = jsonFormat1(Name.apply)
  //  implicit val emailFormat = jsonFormat1(Email.apply)
  //  implicit val userFormat = jsonFormat3(User.apply)

  implicit val userFormat: RootJsonFormat[User] = new RootJsonFormat[User] {
    override def read(json: JsValue): User =
      json.asJsObject.getFields("id", "name", "email") match {
        case Seq(JsString(id), JsString(name), JsString(email)) =>
          User(ID(id), Name(name), Email(email))
        case _ => throw new DeserializationException("User")
      }

    override def write(user: User): JsValue = JsObject(
      "id" -> JsString(user.id.value),
      "name" -> JsString(user.name.value),
      "email" -> JsString(user.email.value)
    )
  }
}
