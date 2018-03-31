# Plugin configuration
The plugins config is stored in a file called [`rpgmenu.config.yml`](https://github.com/joblo2213/RPGMenu/blob/master/src/main/resources/rpgmenu.config.yml) which is located in the plugin folder of BetonQuest, right near the config file of BetonQuest.  
It contains some default settings as well as all messages which are send to the player by the plugin.  
On first start of the plugin the default config file will be created including all default settings which you are then able to change to costumize the plugin.

## The config options
* `config-version`: *(positive integer)*  
  This setting tells the config updater which version yopur config has so he can update old config files to make them always work with the latest release.  
  Therefore its **important** that you **don't change this setting**.

* `debug`: *(boolean)*  
  This determines if debug information should be shown in console.  
  Setting it to true can help spotting some problems with your configuration ore locating errors, but will also spam your console.  
  **Default value:** `false`

* `ingame_update_notifications`:  *(boolean)*  
  By default all players with op rights get notified on join if a new version of the plugin exists.  
  If this annoys you just set this setting to false.  
  **Default value:** `true`

* `default_close`:  *(boolean)*  
  Sets if menus should close by default when a item is clicked (`true`) or if they should stay open (`false`).  
  This can also be overriden by each individual menu.    
  **Default value:** `true`
## The messages section
This section contains all messages which are displayed to the player by the plugin.  
You can change them to fit all your needs.  
Its also possible to add additional languages, it works the same way as with BetonQuests messages.yml:  
Just add another section with the short name of your language as key and the translated messages.  
It's not required to speicfy all messages, if a message is missing for your language it will just pick the message in BetonQuests default language.  

If you added a language which is not yet provided by the plugin please create a [PullRequest](https://github.com/joblo2213/RPGMenu/pulls) so I can include it to make it accessible for all other servers.  

## Updating the config
If you install a new release there is no need for you to spend time on upddating the config.  
The plugin will do that for you.  
It will add default values for all settings which aren't included in your config.  
Before doing so it will create a backup of your old one under `rpgmenu.config_old.yml`.

## The default configuration
```yaml
config-version: 1
debug: false
ingame_update_notifications: true
default_close: true
messages:
  en:
    command_no_permission: '&7[&c&l!&7] No permission!'
    menu_do_not_open: '&7[&c&l!&7] No permission!'
    command_usage: '&cUsage:\n&7{1}'
    command_info_reload: '&breloads all/one menu(s)'
    command_info_list: '&bopens specific menu [for a player]'
    command_info_open: '&blists all loaded menus'
    command_invalid_menu: '&7[&c&l!&7] &c{1}&7 is not a loaded menu!'
    command_no_player: '&7[&c&l!&7] Please specify a player!'
    command_invalid_player: '&7[&c&l!&7] There is no player with the name &c{1}&7!'
    command_no_menu: '&7[&c&l!&7] Please specify a menu!'
    command_open_successful: '&7[&a&li&7]&a Successfully opened &7{1}&a!'
    command_reload_successful: '&7[{1}&li&7]{1} Successfully reloaded &7{2}{1} menu(s)!'
    command_reload_failed: '&7[&4&l!&7]&4 Reload failed!'
    command_list: '&e----- &aRPGMenu &e-----\n&aLoaded menus:&e '
    click_to_open: '&bclick to open menu'
  de:
    command_no_permission: '&7[&c&l!&7] Fehlende Berechtigung!'
    menu_do_not_open: '&7[&c&l!&7] Fehlende Berechtigung!'
    command_usage: '&cVerwendung:\n&7{1}'
    command_info_reload: '&blaedt alle/ein Menue(s) neu  '
    command_info_list: '&boeffnet ein Menue[fuer einen Spieler]'
    command_info_open: '&blistet alle geladenen Menues auf'
    command_invalid_menu: '&7[&c&l!&7] Es ist kein Menue mit der Id &c{1}&7 geladen!'
    command_invalid_player: '&7[&c&l!&7] Es ist kein Spieler mit dem Namen &c{1}&7 online!'
    command_no_player: '&7[&c&l!&7] Bitte gib einen Spieler an!'
    command_no_menu: '&7[&c&l!&7] Bitte gib ein Menue an!'
    command_open_successful: '&7[&a&li&7]&a Menue &7{1}&a wurde geoeffnet!'
    command_reload_successful: '&7[{1}&li&7]{1} Reload erfolgreich! &7{2}{1} Menue(s) wurden geladen!'
    command_reload_failed: '&7[&4&l!&7]&4 Reload fehlgeschlagen!'
    command_list: '&e----- &aRPGMenu &e-----\n&aGeladene Menues:&e '
    click_to_open: '&bklicke um das Menue zu oeffnen'
```