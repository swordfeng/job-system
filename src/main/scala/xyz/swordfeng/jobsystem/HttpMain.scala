package xyz.swordfeng.jobsystem

import com.typesafe.scalalogging.LazyLogging
import fs2.Task
import org.http4s.{HttpService, UrlForm}
import org.http4s.dsl._
import org.http4s.server.blaze._
import io.circe.literal._
import org.http4s.circe._
import org.http4s.util.StreamApp

object HttpMain extends StreamApp with LazyLogging {
    val service = HttpService {
        case req @ POST -> Root / "api" / "login" => req.decode[UrlForm] { f =>
            val username = (f get "username").head
            val password = (f get "password").head
            if (username == null || password == null) {
                Ok(json"""{"status":false,"error":"invalid request"}""")
            } else try {
                val user = User.login(username, password)
                Ok(json"""{"status":true,"username":$username}""")
            } catch {
                case _: User.AuthFailure =>
                    Ok(json"""{"status":false,"error":"authentication failed"}""")
            }
        }
        case req @ POST -> Root / "api" / "register" => req.decode[UrlForm] { f =>
            val username = (f get "username").head
            val password = (f get "password").head
            if (username == null || password == null) {
                Ok(json"""{"status":false,"error":"invalid request"}""")
            } else try {
                val user = User.register(username, password, 0)
                Ok(json"""{"status":true}""")
            } catch {
                case _: User.UserExist =>
                    Ok(json"""{"status":false,"error":"user exists"}""")
            }
        }

        case GET -> Root / "api" / "getFirst" =>
            val job = JobChecker.getCheckingJob
            Ok("")
    }

    override def stream(args: List[String]): fs2.Stream[Task, Nothing] =
        BlazeBuilder.bindLocal(8000).mountService(service, "/").serve
}
