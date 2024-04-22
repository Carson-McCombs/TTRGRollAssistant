
# Example of Home Screen of App

 Each Tab is a "Stat" which can be dependent and modified by other Stats. Here the "Sword Attack" Stat is modified by a "default" or "base" stat "bonus" which increases its value by 5. It is also modified by the "Strength" status that is seen above it.  There is currently only one roll calculation you can make, which is a 20 sided dice roll plus your total bonus to the stat - calculated by floor(( 10 + sum of bonuses ) % 2).  I.e. if the Strength has a total value of 6, the roll would be 1d20 - 2. And if the Strength stat has a value of 21, the dice roll for Strenght would be 1d20 + 5. And when a stat modifies another stat, it always modifies the main stat linearly - i.e. a strenght of 6 would add 6 to the Sword Attack's total and a Strength stat of 21 would add 21 to the Sword Attack's total value. A stat can be modifiedf by more than one stat, but checks are done such that stats can not indirectly or directly modify themselves. In other words, a stat cannot be added that modifiers Strength and is modified by the Sword Attack. The checks to determine the stats and their dependencies are done through recursive SQL ( Kotlin Rooms ) which uses default Rooms calls and custom SQL Lite calls. 


### Picture of Stat Main View

Stats can be minimized for convience. In the Stat main viewm a dice can be rolled based on the stat's total - shown below. The stat total and bonus to roll are shown on this main page.

![image](https://github.com/Carson-McCombs/TTRGRollAssistant/assets/130939367/5ac14404-93aa-435d-8439-b2d88848cc4c)

### Picture of Stat Roll Result

After clicking the dice icon, a random number between 1 and 20 will be rolled with the stat bonus ( calculated by floor(( 10 + sum of bonuses ) % 2) ) added to it - shown below.

![image](https://github.com/Carson-McCombs/TTRGRollAssistant/assets/130939367/9cccc01b-6abc-4eca-98b6-a291d21c411f)

### Picture of Stat Modifiers View

Shows a list of the modifiers that this stat depends on. Each modifier has its total added to the main stat to determine its total and bonus to rolls. Here is also where non-dependent modifiers can be edited where all modifiers can be deleted and added.

![image](https://github.com/Carson-McCombs/TTRGRollAssistant/assets/130939367/eedbdaee-ae30-44c3-a47e-4e24d9f62705)

### Picture of Adding a Stat Dependent Modifier

Modifiers can either be set by value or by referencing a dependent stat, in which case the values are fill in and cannot be set. The non-dependent modifiers can be edited from the modifiers page.

![image](https://github.com/Carson-McCombs/TTRGRollAssistant/assets/130939367/607d7a48-ed4b-4919-b5a0-dffbe1cd6349)

### Picture of Stat Settings Page

Here is where the name of the stat can be set and where the stat can be deleted.

![image](https://github.com/Carson-McCombs/TTRGRollAssistant/assets/130939367/bdb2f1f6-8c9b-46fb-9a9a-2263f2b9f053)

Note: This does not necessarily represent all dice rolls within TTRPGs. This was just one of my prototypes for what the UI/UX might look like. My next version will instead implement a Text Parser and Calculator with basic logic so that more complex relationships can be held.
