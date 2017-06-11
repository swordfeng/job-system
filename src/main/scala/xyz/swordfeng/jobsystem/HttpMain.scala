package xyz.swordfeng.jobsystem

import com.typesafe.scalalogging.LazyLogging
import fs2.Task
import org.http4s.{HttpService, StaticFile, UrlForm}
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
            val money = (f get "money").head.toInt
            if (username == null || password == null) {
                Ok(json"""{"status":false,"error":"invalid request"}""")
            } else try {
                val user = User.register(username, password, money)
                Ok(json"""{"status":true}""")
            } catch {
                case _: User.UserExist =>
                    Ok(json"""{"status":false,"error":"user exists"}""")
                case _: User.MoneyBelowZero =>
                    Ok(json"""{"status":false,"error":"no money no register"}""")
            }
        }

        case req @ POST -> Root / "api" / "pass" =>
            if (JobChecker.checkingJobPassed(true)) {
                Ok(json"""{"status": true}""")
            } else {
                Ok(json"""{"status": false, "error": "no checking job"}""")
            }

        case req @ POST -> Root / "api" / "reject" =>
            if (JobChecker.checkingJobPassed(false)) {
                Ok(json"""{"status": true}""")
            } else {
                Ok(json"""{"status": false, "error": "no checking job"}""")
            }

        case req @ GET -> Root / "api" / "getFirst" =>
            val job = JobChecker.getCheckingJob
            if (job != null) {
                Ok(json"""{
                  "status": true,
                  "name": ${job.getName},
                  "address": ${job.getAddress},
                  "education": ${job.getEducation},
                  "num": ${job.getRequiredNumOfPeople},
                  "skills": ${job.getSkills}
                  }""")
            } else {
                Ok(json"""{"status":false,"error":"no checking jobs"}""")
            }

        case req @ POST -> Root / "api" / "submitJob" => req.decode[UrlForm] { f =>
            try {
                val user = User.get((f get "username").head)
                val job = new Job(
                    user,
                    (f get "name").head,
                    (f get "address").head,
                    (f get "num").head.toInt,
                    (f get "skills").toArray,
                    (f get "education").head
                )
                job.save()
                Ok(json"""{"status": true}""")
            } catch {
                case _: Throwable => Ok(json"""{"status": false, "error": "wtf"}""")
            }
        }

        case req @ GET -> Root / path if !path.startsWith("api/") =>
            StaticFile.fromResource("/ui/" + path, Some(req)).map(Task.now).getOrElse(NotFound())
    }

    override def stream(args: List[String]): fs2.Stream[Task, Nothing] =
        BlazeBuilder.bindLocal(8000).mountService(service, "/").serve
}
