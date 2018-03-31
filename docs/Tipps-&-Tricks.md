## Creating quest menus
To create a menu that gives the player a overview of his open quests just define one menu item for each quest.
Set the [conditions](Menu#the-items-section) for this item so it is only displayed if the quest is not finished (use the [tag condition](https://github.com/Co0sh/BetonQuest/wiki/Conditions-List#tag-tag)).  
Then assign all those items to [a row of slots](Menu#the-slots-section) so that they are sorted perfectly.

You can also add click events to display npc locations, add compass targets, directly open the conversations or cancel the quest. 

Or you could define separate items for open and finished quests or even to show the progress. Just be a bit creative.

## Menus displaying players stats
You may also use menus to display the stats of a player. Just use [variables](https://github.com/Co0sh/BetonQuest/wiki/Variables-List) in the text or for the amount of an item. 

For example try displaying a players money using the varible from [Vault integration](http://dev.bukkit.org/bukkit-plugins/vault/) or use [PlaceholderAPI](https://github.com/Co0sh/BetonQuest/wiki/Compatibility#placeholderapi) to show placeholders from many other plugins.

## Naming conventions
Just a tip from my side: I would call events, conditions and items which are used by menus `menu_` followed by the name of your menu, followed by `_` and then the name of the condition/event/item.
That way it is later easy to find them when looking through your files.  

Of course this is just an advice, it's totally up to you how you call them. Feel free to develop you own system.  