import akka.actor.Actor
import spray.routing.HttpService
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._
import spray.http.MediaTypes

case class Stuff(id: Int, data: String)

object Stuff extends DefaultJsonProtocol {
  implicit val stuffFormat = jsonFormat2(Stuff.apply)
}

class SampleServiceActor extends Actor with SampleRoute {
  def actorRefFactory = context
  def receive = runRoute(route)
}

trait SampleRoute extends HttpService {
  val route = {
    pathPrefix("offer"){
    pathPrefix("stuff") {
      pathEnd{
        respondWithMediaType(MediaTypes.`application/json`) {
          get { complete(Stuff(1, "my stuff")) } ~
            post { entity(as[Stuff]) { stuff => println("test");complete(Stuff(stuff.id + 100, stuff.data + " posted"))}}
        } ~ get { complete("I exist!")}
    }} ~
    pathPrefix("params") {
      pathEnd{
        get {parameters('req, 'opt.?) { (req, opt) => complete(s"Req: $req, Opt: $opt")}}
      }
    }
    } ~ pathPrefix("headers") {pathEnd{
      get {
        headerValueByName("ct-remote-user") { userId =>
          complete(userId)
        }
      }
  }}}





}
