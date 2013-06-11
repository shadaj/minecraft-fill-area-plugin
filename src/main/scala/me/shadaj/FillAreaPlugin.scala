package me.shadaj

import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.PluginManager
import org.bukkit.Location

class FillAreaPlugin extends ScalaPlugin {
  var start: Option[Location] = None
  var end: Option[Location] = None

  var selectingStart = false
  var selectingEnd = false

  val blockPlace = (e: BlockPlaceEvent) => {
    if (selectingStart) {
      e.getPlayer.sendMessage("Selected a start block for filling")
      val blockPlaced = e.getBlockPlaced
      start = Some(blockPlaced.getLocation())
      selectingStart = false
      selectingEnd = false
    } else if (selectingEnd) {
      e.getPlayer.sendMessage("Selected an end block for filling")
      val blockPlaced = e.getBlockPlaced
      end = Some(blockPlaced.getLocation())
      selectingStart = false
      selectingEnd = false
    } else {

    }
  }

  override def onEnable {
    getServer.getPluginManager.registerEvents(blockPlace, this)
  }

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    val player = sender.asInstanceOf[Player]
    val commandName = command.getName

    if (commandName == "selectStart") {
      selectingStart = true
      player.sendMessage("Selecting mode enabled for filling")
    } else if (commandName == "selectEnd") {
      selectingEnd = true
      player.sendMessage("Selecting mode enabled for filling")
    } else if (commandName == "setStart") {
      val x = args(0).toInt
      val y = args(1).toInt
      val z = args(2).toInt

      player sendMessage "Setting start location"
      start = Some(new Location(player.getWorld(), x, y, z))
    } else if (commandName == "setEnd") {
      val x = args(0).toInt
      val y = args(1).toInt
      val z = args(2).toInt

      player sendMessage "Setting end location"
      end = Some(new Location(player.getWorld(), x, y, z))
    } else if (commandName == "fill") {
      player sendMessage "Filling blocks"

      val world = player.getWorld
      val id = args(0).toInt

      if (!start.isDefined || !end.isDefined) {
        player.sendMessage("You have not selected both a start and end")
      } else {
    	val s = start.get
    	val e = end.get
        for (
          x <- (s.getBlockX min e.getBlockX) to (s.getBlockX max e.getBlockX);
          y <- (s.getBlockY min e.getBlockY) to (s.getBlockY max e.getBlockY);
          z <- (s.getBlockZ min e.getBlockZ) to (s.getBlockZ max e.getBlockZ)
        ) {
          val blockToEdit = world.getBlockAt(x, y, z)

          blockToEdit.setTypeId(id)
        }
      }
    }
    true
  }
}