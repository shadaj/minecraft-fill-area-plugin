package shadaj

import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.PluginManager
import BukkitImplicits._

class FillAreaPlugin extends JavaPlugin {
  var start = (0, 0, 0)
  var end = (0, 0, 0)

  var selectingStart = false
  var selectingEnd = false

  val blockPlace = (e: BlockPlaceEvent) => {
    if (selectingStart) {
      e.getPlayer.sendMessage("Selected a start block for filling")
      val blockPlaced = e.getBlockPlaced
      val location = (blockPlaced.getX, blockPlaced.getY, blockPlaced.getZ)
      start = location
      selectingStart = false
      selectingEnd = false
    } else if (selectingEnd) {
      e.getPlayer.sendMessage("Selected an end block for filling")
      val blockPlaced = e.getBlockPlaced
      val location = (blockPlaced.getX, blockPlaced.getY, blockPlaced.getZ)
      end = location
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
      start = (x, y, z)
    } else if (commandName == "setEnd") {
      val x = args(0).toInt
      val y = args(1).toInt
      val z = args(2).toInt

      player sendMessage "Setting end location"
      end = (x, y, z)
    } else if (commandName == "fill") {
      player sendMessage "Filling blocks"

      val world = player.getWorld
      val id = args(0).toInt

      for (
        x <- (start._1 min end._1) to (start._1 max end._1);
        y <- (start._2 min end._2) to (start._2 max end._2);
        z <- (start._3 min end._3) to (start._3 max end._3)
      ) {
        val blockToEdit = world.getBlockAt(x, y, z)

        blockToEdit.setTypeId(id)
      }
    }
    true
  }
}