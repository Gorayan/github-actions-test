package net.gorayan.mc.exposedsample

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object User : Table() {
    val mcid = varchar("mcid", 16)
    val uuid = uuid("uuid").primaryKey()
    val login = datetime("login")
}