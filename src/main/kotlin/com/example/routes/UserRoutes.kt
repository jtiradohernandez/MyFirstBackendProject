package com.example.routes

import io.ktor.server.routing.*
import com.example.models.User
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


private val user = mutableListOf<User>()

fun Route.userRouting() {
    route("/user") {
        get {  //buscar que el usuario existe
            if (user.isEmpty()){
                call.respondText ("No existen usuarios en la base de datos", status = HttpStatusCode.OK)
            }else {
                call.respond(user)
            }
        }

        post{// crear un nuevo usuario
            val newUser = call.receive<User>()
            user.add(newUser)
            call.respondText("Usuario creado correctamente", status = HttpStatusCode.Created)
        }

        delete( "{id?}") {//borrar el usuario
            val id = call.parameters["id"] ?: return@delete call.respondText("Falta id del usuario", status = HttpStatusCode.BadRequest)
            val userDeleted = user.find { it.userID == id.toInt() } ?: return@delete call.respondText("No existe el usuario", status = HttpStatusCode.NotFound)
            user.remove(userDeleted)
            call.respondText("Usuario eliminado correctamente", status = HttpStatusCode.OK)
        }
    }
    route("/saldo"){
        get( "{id?}"){//EP para obtener el saldo
            val id = call.parameters["id"] ?: return@get call.respondText ("UserID no encontrado en la peticion", status = HttpStatusCode.BadRequest )
            val userId = user.find {it.userID == id.toInt()} ?: return@get call.respondText ("Este usuario no está registrado", status = HttpStatusCode.NotFound)
            call.respond(userId.saldo)
        }

        post{//EP para modificar el saldo
            //val params = call.receiveParameters()
            val id = call.request.queryParameters["id"] ?: return@post call.respondText("Falta el userID en la peticion", status = HttpStatusCode.BadRequest)
            val amount = call.request.queryParameters["amount"] ?: return@post call.respondText("Falta la cantidad en la peticion", status = HttpStatusCode.BadRequest)
            val userid = user.find { it.userID == id.toInt()} ?: return@post call.respondText("Usuario no encontrado", status = HttpStatusCode.NotFound)
            val indice = user.indexOf(userid)
            user[indice].saldo = user[indice].saldo + amount.toInt()
            val cantidad = user[indice].saldo
            call.respondText("Tu nuevo saldo es $cantidad", status = HttpStatusCode.OK)
        }
    }
}