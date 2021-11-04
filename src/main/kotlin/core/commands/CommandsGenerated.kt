package core.commands

class CommandsGenerated : CommandsCollection {
    override val values: List<core.commands.Command> = listOf(combat.attack.AttackCommand(), combat.block.BlockCommand(), combat.dodge.DodgeCommand(), conversation.SpeakCommand(), core.commands.UnknownCommand(), crafting.checkRecipe.RecipeCommand(), crafting.craft.CookCommand(), crafting.craft.CraftRecipeCommand(), explore.examine.ExamineCommand(), explore.listen.ListenCommand(), explore.look.LookCommand(), explore.map.ReadMapCommand(), explore.map.compass.ViewCompassCommand(), inventory.EquippedCommand(), inventory.InventoryCommand(), inventory.dropItem.DropItemCommand(), inventory.equipItem.EquipItemCommand(), inventory.equipItem.HoldItemCommand(), inventory.pickupItem.TakeItemCommand(), inventory.putItem.PutItemCommand(), inventory.unEquipItem.UnEquipItemCommand(), magic.castSpell.CastCommand(), quests.journal.JournalCommand(), status.rest.RestCommand(), status.status.StatusCommand(), system.ExitCommand(), system.RedoCommand(), system.alias.AliasCommand(), system.debug.DebugCommand(), system.help.CommandsCommand(), system.help.HelpCommand(), system.history.HistoryCommand(), system.persistance.changePlayer.PlayAsCommand(), system.persistance.loading.LoadCommand(), system.persistance.newGame.CreateNewGameCommand(), system.persistance.saving.SaveCommand(), time.ViewTimeCommand(), traveling.approach.ApproachCommand(), traveling.approach.RetreatCommand(), traveling.climb.ClimbCommand(), traveling.climb.DismountCommand(), traveling.jump.JumpCommand(), traveling.move.MoveCommand(), traveling.routes.RouteCommand(), traveling.travel.TravelCommand(), traveling.travel.TravelInDirectionCommand(), use.UseCommand(), use.eat.EatCommand(), use.interaction.nothing.NothingCommand())
}