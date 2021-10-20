package net.gorayan.mc.exposedsample

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExposedSample : JavaPlugin(), Listener {

    override fun onEnable() {

        Database.connect("jdbc:mysql://localhost/exposed_sample", driver = "com.mysql.jdbc.Driver", user = "gorayan", password = "password")
        transaction {
            //SchemaUtils.create(User)
            SchemaUtils.drop(User)

        }

        Bukkit.getPluginManager().registerEvents(this, this)

    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {

        val player = event.player

        transaction {

            Thread.sleep(10000)

            val user = User.select { User.uuid eq player.uniqueId }
            if (user.count() > 0L) {
                player.sendMessage("ようこそ！前回のログインは${user.single()[User.login].format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))}です！")
                User.update({ User.uuid eq player.uniqueId }) { it[login] = LocalDateTime.now() }
                return@transaction
            }

            User.insert {
                it[mcid] = player.name
                it[uuid] = player.uniqueId
                it[login] = LocalDateTime.now()
            }
            player.sendMessage("初めまして${player.name}さん！")

        }
    }

}