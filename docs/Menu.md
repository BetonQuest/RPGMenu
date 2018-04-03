# Creating a menu
After installing the plugin a menu folder is created in each package.
To create a new menu just create a new `.yml` file in this folder and the plugin will automatically detect it on reload.
The id which can be used to identify each menu will be the same as the filename, but without the `.yml` extension.
Therefore you shouldn't use `.` characters in the filename.
Make shure that you are using `UTF8` as file encoding.
You can check if the syntax of your config is correct by using [this tool](http://www.yamllint.com/).

The following section will tell you about each setting which has to be set (or can optionally be set) to configure your menu.
# The menu settings
* `title`: *(string)*  
  Fist of all you have to set the title of your menu. 
  This will be displayed in the top left corner of your menu.
  You can use [Formattingcodes](https://minecraft.gamepedia.com/Formatting_codes) with `&` instead of `§` to color the 
title.  
  **Example:** `title: '&6&lQuests'`

* `height`: *(integer `>0`, `<7`)*  
  This sets how many lines of slots your menu will have.  
  **Example:** `height: 3`

* `bind`: *([quest item](https://github.com/Co0sh/BetonQuest/wiki/Reference#items))* ***-optional***   
  You can use this to bind the menu to an item, so that it opens if the player clicks while holding it.
  The item should be defined in the items.yml and you should specify its id here. Use `packageName.id` to use items from other packages.
  This setting is optional so you can leave it out to bind no item.  
  **Example:** `bind: menu_quests_open`

* `command`: *(string)* ***-optional***  
  You can use this to create a new command which opens the menu if it is executed by a player.
  The command should only contain letters, numbers and `-`. Spaces are not allowed.  
  **Example:** `command: '/quests'`
    
* `open_conditions`: *(string)* ***-optional***  
  You can add a list of conditions, each one separated by a `,` which all have to be true to open the menu with a bound item or a bound command. Use `packageName.id` to use conditions from other packages and `!` before the id to invert the condition.   
  **Example:** `open_conditions: 'menu_quests_open_tag,!sneaking'`

* `open_events`: *(string)* ***-optional***  
  You can add a list of events, each one separated by a `,` which all are run when the menu opens. Use `packageName.id` to use events from other packages.   
  **Example:** `open_events: 'menu_quests_open_addpoints,menu_quests_open_effect_resistance_short'`

* `close_events`: *(string)* ***-optional***  
  You can add a list of events, each one separated by a `,` which all are run when the menu is closed. Use `packageName.id` to use events from other packages.   
  **Example:** `close_events: 'menu_quests_reopen'`

* `items`: *(configuration section)*   
  In this section you define the items which should be displayed in the menu.
  Have a look at [the items section](#the-items-section) part of the wiki which is covering this topic.

* `slots`: *(configuration section)*   
  In this section you define which items form the `items` sction should be displayed in which slot.
  Have a look at [the slots section](#the-slots-section) part of the wiki which is covering this topic.

## The `items` section
The items section contians all items which should be displayed in the menu, defined as individual sections of the config. The names of the items should only contain numbers, letters hyphens and underscores. You shouldn't name a item `yes`, `no`, `true` or `false` as those are yml keywords for booleans.  
Each item has the following settings:

* `item`: *([quest item](https://github.com/Co0sh/BetonQuest/wiki/Reference#items))*  
  The id of a quest item as defined in the `items.yml`. This item will be displayed in the menu.
  Use `packageName.id` to display the item from another package.   
  **Example:** `item: menu_quests_dia`

* `amount`: *(variable number)*  ***-optional***  
  The size of the stack that will be displayed in the menu.
  You can also use [variables](https://github.com/Co0sh/BetonQuest/wiki/Variables-List) here to have the amount based on  a variable. Amount is optional so you can leave it out to have a stack size of `1`.  
  **Example:** `amount: '%point.reputation.amount%'`

* `condition`/`conditions`: *(string)*  ***-optional***  
   You can define one or multiple conditions (seperated by `,`) which all have to be true so that the item is displayed in the menu. Use `packageName.id` to use conditions from other packages and `!` before the id to invert the condition.  
  **Example:** `condition: tag_completed`  
  
* `click`: *(string)*  ***-optional***    
  You can define events (each one separated by `,`) that are run whenever the item is clicked. Use `packageName.id` to use events from other packages.  
  **Example:** `click: start_quest_wood`    

  If you want to have other events when you left click than when you right click create a section with the two settings:  
   * `left`: events that are run on left click
   * `right`: events that are run on right click

  **Example:** 
  ```
  click:
      left: 'give_xp,msg_give_xp'
      right: 'take_xp,msg_take_xp'
  ```    
* `close`: *(boolean)* ***-optional***   
  If the menu should close after the item was clicked. If this is not set the `default_close` value from the plugins config will be used.  
  **Example:** `close: false`

*  `text`: *(list of strings)*  
  In the text section you can specify the text that is displayed if you hover over the item.
  The lore and the display name of the item specified in `items.yml` will be overwritten by this. 
  You can use [formatting codes](https://minecraft.gamepedia.com/Formatting_codes) with `&` and [variables](https://github.com/Co0sh/BetonQuest/wiki/Variables-List)
  
   **Example:**
   ```
    text:
      - '&2Quest reputation: &6&l%point.quest_reputation.amount%'
   ```
   Also like the text in conversations you can provide translations for all languages:
   ```
   text:
      en:
        - '&7[Quest] &6&lThe lost amulet'
        - '&4&o'
        - '&eLeft click to locate npc'
        - '&eRight click to cancel quest'
      de:
        - '&7[Quest] &6&lDas verlorene Amulet'
        - '&4&o'
        - '&eLinksclick um den NPC zu finden'
        - '&eRechstclick um die Quest abzubrechen'
   ```

### An example for a `items` section:
```yml
items:
  quest1_active:
    item: menu_quests_1
    amount: 1
    text:
      - '&7[Quest] &f&lBone ripper'
      - '&f&o'
      - '&eLeft click to locate npc'
      - '&eRight click to cancel quest'
    click:
      right: menu_quests_1_cancel,menu_quests_1_cancel_msg
      left: menu_quests_1_compass,menu_quests_1_location_msg
    close: true
  reputation:
    item: menu_quests_reputation
    amount: 1
    text:
      - '&2Quest reputation: &6&l%point.quest_reputation.amount%'
    close: false
```
## The `slots` section
In the slots section you define which items from the items section should be displayed in which slot.  
On [this image](http://wiki.vg/images/b/bb/DoubleChest-slots.png) you can easily find out the number for each slot.  
You can also assign multiple items to the same slot and use conditions in [the items section](#the-items-section) specify which one should be used.
If you assign multiple items the first one for which all conditions are true will be displayed.

Another thing you can do is assigning multiple items to a row of slots. Now the slots are filled up one by one with the items from the list which have all conditions returning true.  
To make this more clear I want to show you this example:  
`0-2: 'quest1,quest2,quest3'`  
Assuming that the conditions for the items say that quest1 and quest3 should be displayed to the player but quest2 shouldn't then quest1 would be in the slot 0 and quest3 in the slot 1. Slot 2 would stay empty.

In addition you can also assign items to a rectangle of slots. Just like with the row the slots in this rectangle are filled up one by one with the items from the list which have all conditions returning true.  
Here is a example:
`14*25: 'quest1,quest2,quest3'`

![rectangle-example](https://cdn.discordapp.com/attachments/420985429840625676/430808651545182208/Unbenannt.png)

### An example for a `slots` section:
```yml
slots:
  8: reputation
  11-13: 'quest1_active,quest2_active,quest3_active'
```

# Example menu

```yml
height: 3
title: '&6&lQuests'
bind: menu_quests_open
command: '/quests'
open_conditions: 'menu_quests_open_permission'
items:
  quest1_active:
    item: menu_quests_1
    amount: 1
    conditions: '!menu_quests_1_tag_completed'
    text:
      en:
        - '&7[Quest] &f&lBone ripper'
        - '&f&oRipp some skeletons off'
        - '&f&otheir bones to complete'
        - '&f&othis quest.'
        - '&f&o'
        - '&eLeft click to locate npc'
        - '&eRight click to cancel quest'
      de:
        - '&7[Quest] &f&lKnochenbrecher'
        - '&f&oBrech den Skeletten alle'
        - '&f&oKnochen um diese Quest'
        - '&f&oabzuschließen'
        - '&f&o'
        - '&eLinksclick um den NPC zu finden'
        - '&eRechstclick um die Quest abzubrechen'
    click:
      right: menu_quests_1_cancel,menu_quests_1_cancel_msg
      left: menu_quests_1_compass,menu_quests_1_location_msg
    close: true
  quest1_completed:
    item: menu_quests_1_completed
    amount: 1
    conditions: menu_quests_1_tag_completed
    text:
      en:
        - '&2[Quest] &f&lBone ripper'
        - '&f&oRipp some skeletons off'
        - '&f&otheir bones to complete'
        - '&f&othis quest.'
        - '&f&o'
        - '&2Quest completed!'
      de:
        - '&2[Quest] &f&lKnochenbrecher'
        - '&f&oBrech den Skeletten alle'
        - '&f&oKnochen um diese Quest'
        - '&f&oabzuschließen'
        - '&f&o'
        - '&2Quest abgeschlossen!'
    close: false
  quest2_active:
    item: menu_quests_2
    amount: 1
    conditions: '!menu_quests_2_tag_completed'
    text:
      en:
        - '&7[Quest] &7&lSorcerer''s apprentice'
        - '&d&oCollect some ingredients'
        - '&d&ofor the sorcerer to'
        - '&d&ocomplete this quest.'
        - '&d&o'
        - '&eLeft click to locate npc'
        - '&eRight click to cancel quest'
      de:
        - '&7[Quest] &7&lZauberlehrling'
        - '&d&oSammel für den Zauberer'
        - '&d&oeinige Trankzutaten um '
        - '&d&odiese Quest abzuschließen.'
        - '&d&o'
        - '&eLinksclick um den NPC zu finden'
        - '&eRechstclick um die Quest abzubrechen'
    click:
      right: menu_quests_2_cancel,menu_quests_2_cancel_msg
      left: menu_quests_2_compass,menu_quests_2_location_msg
    close: true
  quest2_completed:
    item: menu_quests_2_completed
    amount: 1
    conditions: menu_quests_2_tag_completed
    text:
      en:
        - '&2[Quest] &7&lSorcerer''s apprentice'
        - '&d&oCollect some ingredients'
        - '&d&ofor the sorcerer to'
        - '&d&ocomplete this quest.'
        - '&d&o'
        - '&2Quest completed!'
      de:
        - '&2[Quest] &7&lZauberlehrling'
        - '&d&oSammel für den Zauberer'
        - '&d&oeinige Trankzutaten um '
        - '&d&odiese Quest abzuschließen.'
        - '&d&o'
        - '&2Quest abgeschlossen!'
    close: false
  quest3_active:
    item: menu_quests_3
    amount: 1
    conditions: '!menu_quests_3_tag_completed'
    text:
      en:
        - '&7[Quest] &6&lThe lost amulet'
        - '&4&oFind the lost amulet of'
        - '&4&oSonequa to complete '
        - '&4&othis quest.'
        - '&4&o'
        - '&eLeft click to locate npc'
        - '&eRight click to cancel quest'
      de:
        - '&7[Quest] &6&lDas verlorene Amulet'
        - '&4&oFinde das verlorene Amulet'
        - '&4&ovon Sonequa um diese'
        - '&4&oQuest abzuschließen.'
        - '&4&o'
        - '&eLinksclick um den NPC zu finden'
        - '&eRechstclick um die Quest abzubrechen'
    click:
      right: menu_quests_3_cancel,menu_quests_3_cancel_msg
      left: menu_quests_3_compass,menu_quests_3_location_msg
    close: true
  quest3_completed:
    item: menu_quests_3_completed
    amount: 1
    conditions: menu_quests_3_tag_completed
    text:
      en:
        - '&2[Quest] &6&lThe lost amulet'
        - '&4&oFind the lost amulet of'
        - '&4&oSonequa to complete '
        - '&4&othis quest.'
        - '&4&o'
        - '&2Quest completed!'
      de:
        - '&2[Quest] &6&lDas verlorene Amulet'
        - '&4&oFinde das verlorene Amulet'
        - '&4&ovon Sonequa um diese'
        - '&4&oQuest abzuschließen.'
        - '&4&o'
        - '&2Quest abgeschlossen!'
    close: false
  reputation:
    item: menu_quests_reputation
    amount: 1
    text:
      en:
        - '&2Quest reputation: &6&l%point.quest_reputation.amount%'
      de:
        - '&2Quest Ansehen: &6&l%point.quest_reputation.amount%'
    click: menu_quests_reputation_msg
    close: true
slots:
  8: reputation
  11: 'quest1_active,quest1_completed'
  13: 'quest2_active,quest2_completed'
  15: 'quest3_active,quest3_completed'
```
> **How the menu looks ingame:**  
> ![image](https://cdn.discordapp.com/attachments/420985429840625676/424927791948234772/giphy.gif)