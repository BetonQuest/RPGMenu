# Commands
On this page you find all commands for the plugin.

## Main plugin command: `/rpgmenu`
**Aliases:** `/rpgmenus`, `/menu`, `/menus`, `/rpgm`, `/qm`

**Premission:** `betonquest.admin`

**Description:**  
Provides various utillity commands for the plugin.

**Subcommands:**  

* `/rpgmenu reload [menu]`:  
  Allows reloading all configuration files or just reloading the configuration of one specific menu.

* `/rpgmenu list`:  
  Lists all currently loaded menus and allows opening them just by clicking on them.

*  `/rpgmenu open <menu> [player]`:  
   Opens a menu for you or another player. [Opening conditions](Menu#the-menu-settings) of the menu will be ignored when using this command.  

## Bound commands: *(costumizable)*
The plugin lets you create a new command for each menu which allows all players to open the menu.  
You can also specify BetonQuest conditions so that the menu can only be opened if the player matches specific conditions (like has tags, permissions or points).
Have a look at the [menu settings](Menu#the-menu-settings) for more information.