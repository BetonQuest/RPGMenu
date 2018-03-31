![header](https://raw.githubusercontent.com/joblo2213/RPGMenu/master/header.png)
# RPGMenu [![Build Status](https://travis-ci.org/joblo2213/RPGMenu.svg?branch=master)](https://travis-ci.org/joblo2213/RPGMenu)

RPGMenu is a addon for [BetonQuest](https://github.com/Co0sh/BetonQuest) which allows server operators to simply create thier own guis
by using events and items from BetonQuest.  
Nearly everything can be done by using this addon, from simple selection, over warp systems, guis listing open quests to information dialogs that display player stats.
For all this you only have to be familliar with BetonQuests events/conditions system, no coding skills are required.
Everything can be created by simply creating and modifying config files in YAML.

## Features 
* Spigot **1.9**, **1.10**, **1.11** and **1.12** support
* Completly **costumizable** guis
* **Display** or **hide** items in guis based on **conditions**
* Run **events** when items are clicked, menus are opened or closed 
* **Multilanguage support**: Easily translate your menus to all languages you want
* Use **variables** to display quest progress or player stats from other plugins (PlaceholderAPI) 
* Bind menus to quest items or commands to open them by clicking a item/running a custom command
* Active, open source project 
* [**Wiki**](https://github.com/joblo2213/RPGMenu/wiki) documenting all plugin features

## Small example:
> ![example](https://camo.githubusercontent.com/4f15b2b4ec801f0cd9f7e6e314b72f1b198d5468/68747470733a2f2f63646e2e646973636f72646170702e636f6d2f6174746163686d656e74732f3432303938353432393834303632353637362f3432343932373739313934383233343737322f67697068792e676966)  
> *The config for this example can be found [here](https://github.com/joblo2213/RPGMenu/wiki/Menu#example-menu).*

## Getting started 
Head over to **[releases](https://github.com/joblo2213/RPGMenu/releases)** where you can find all releases of the plugin.
To install it drop the jar in your plugins folder. 
The plugin will automatically create a [config](https://github.com/joblo2213/RPGMenu/wiki/Config#plugin-configuration) called `rpgmenu.config.yml` in your BetonQuest folder and create menu folders in all packages.
Have a look at the [**Wiki**](https://github.com/joblo2213/RPGMenu/wiki) to learn how to creat your first menus.

## Discord Server
I have a discord server where I provide fast support for all my plugins:

[![discord](https://discordapp.com/api/guilds/401874838504865792/widget.png?style=banner2)](https://discord.gg/yfCDhMb)  

I'll also post some updates about current development progress there and we may discuss about new ideas for the plugin.  
There also is a channel dedicated to sharing your menus so maybe you'll find some helpfull examples by other useres there or wan't to share your own work.
So what are you waiting for? **Join our community where everyone helps each other!**  
**This even works without creating a new account**, just follow the link, choose a nickname and start writing with us.

## BetonQuest [Editor](https://github.com/Co0sh/BetonQuest-Editor) and [Uploader](https://github.com/Co0sh/BetonQuestUploader)
The uploader is completly compatible to this addon.  
Just zip the packages where you defined your menus and upload them as discribed [here](https://github.com/Co0sh/BetonQuestUploader#as-betonquest-editor-export-tool). 

I currently can't implement this into the BetonQuest-Editor so for now you have to edit the config files directly.

## Bugs & Features
To report bugs please create a issue with [this template](https://github.com/joblo2213/RPGMenu/issues/new?template=bug_template.md&labels=Bug).

If you have some ideas how to improve the plugin please let me know with [this template](https://github.com/joblo2213/RPGMenu/issues/new?template=feature_request.md&labels=Feature).

I'm always happy to get this kind of feedback, it really helps improving the plugin.
If you use the provided templates this saves me a little work. 😄

## Contributing 
There are multiple ways you can contribute work to this project:

If you are experienced with Java and Spigot plugin development and have a idea for a feature or found a bug feel free to implement/fix it and create a pull request.
You don't have to mess with formating, I'll do that later for you, but I would really apreciate if you could ad some method comments.

If Java is completely new territory for you, you can still contribute your part:
Translate the plugins messages to your language and create a pull request or contribute to the wiki by editing the files in the docs directory and creating a pull request.

I'm happy about everyone who helps^^

## Support me
It would help a lot if you could leave a review on the plugins spigot site. *(currently not available)*

If this plugin is really usefull for you and you want you may send me a small donation via [tipeeestream](https://www.tipeeestream.com/ung3froren/donation).

## Credits
**A huge "THANK YOU!" goes to:** 
*  my buddy **Windack** for creating the awesome header and the icon.
*  [**@Co0sh**](https://github.com/Co0sh) for creating BetonQuest, the best quest plugin out there.
*  **all people** who help improving the plugin with bug reports and suggesting issues
